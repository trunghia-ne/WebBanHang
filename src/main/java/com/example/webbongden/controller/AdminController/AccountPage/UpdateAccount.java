package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.AccountDao; // NEW: Import AccountDao để lấy danh sách vai trò
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Role;    // NEW: Import model Role
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.utils.LogUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
// Sử dụng Jackson 1.x như code gốc của bạn
import org.codehaus.jackson.map.ObjectMapper;
// Nếu bạn muốn nâng cấp lên Jackson 2.x, các import sẽ là:
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;
import org.mindrot.jbcrypt.BCrypt; // NEW: Import BCrypt để hash mật khẩu

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap; // NEW: Import HashMap
import java.util.List;    // NEW: Import List
import java.util.Map;     // NEW: Import Map
import java.util.Optional;  // NEW: Import Optional

@WebServlet(name = "UpdateAccount", value = "/update-account")
public class UpdateAccount extends HttpServlet {
    private static final AccountServices accountServices;
    private static final AccountDao accountDao; // NEW: Khai báo AccountDao

    static {
        accountServices = new AccountServices();
        accountDao = new AccountDao(); // NEW: Khởi tạo AccountDao
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper(); // Hoặc new com.fasterxml.jackson.databind.ObjectMapper();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            String accountIdParam = request.getParameter("id");
            if (accountIdParam == null || accountIdParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("message", "ID tài khoản không hợp lệ.");
                response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                return;
            }

            int accountId = Integer.parseInt(accountIdParam);
            Account account = accountServices.getAccountById(accountId);

            if (account == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jsonResponse.put("message", "Không tìm thấy tài khoản với ID được cung cấp.");
                response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                return;
            }

            // Tạo Map để chỉ gửi về các thông tin cần thiết và đúng định dạng
            Map<String, Object> accountData = new HashMap<>();
            accountData.put("id", account.getId());
            accountData.put("cusName", account.getCusName());
            accountData.put("username", account.getUsername());
            accountData.put("email", account.getEmail());
            // Quan trọng: Gửi về roleName để JavaScript có thể chọn đúng trong dropdown
            accountData.put("role", account.getRoleName());
            // Định dạng ngày tháng nếu cần, ví dụ: "yyyy-MM-dd"
            accountData.put("createdAt", account.getCreatedAt() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(account.getCreatedAt()) : "");
            // Không gửi mật khẩu về client

            response.getWriter().write(mapper.writeValueAsString(accountData));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("message", "ID tài khoản phải là số.");
            response.getWriter().write(mapper.writeValueAsString(jsonResponse));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("message", "Đã xảy ra lỗi trong quá trình xử lý: " + e.getMessage());
            response.getWriter().write(mapper.writeValueAsString(jsonResponse));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper(); // Hoặc new com.fasterxml.jackson.databind.ObjectMapper();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse JSON request body. Jackson sẽ map "role":"admin" vào accountFromRequest.setRole("admin")
            Account accountFromRequest = mapper.readValue(jsonBuilder.toString(), Account.class);

            // Lấy thông tin tài khoản hiện tại từ DB để so sánh và giữ lại các giá trị cũ nếu cần
            Account accountInDB = accountServices.getAccountById(accountFromRequest.getId());
            if (accountInDB == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không tìm thấy tài khoản để cập nhật (ID: " + accountFromRequest.getId() + ").");
                response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                return;
            }

            // Tạo một bản sao của accountInDB để ghi log beforeData
            Account beforeUpdateAccount = accountServices.getAccountById(accountFromRequest.getId());


            // 1. Xử lý cập nhật vai trò (Role)
            // accountFromRequest.getRole() sẽ trả về tên vai trò dạng String (ví dụ: "admin", "customer")
            // do Jackson đã map từ JSON {"role": "admin"}
            String newRoleNameFromRequest = accountFromRequest.getRole();
            if (newRoleNameFromRequest != null && !newRoleNameFromRequest.trim().isEmpty()) {
                List<Role> allRoles = accountDao.getAllRoles(); // Cần có phương thức này trong AccountDao
                Optional<Role> targetRoleOpt = allRoles.stream()
                        .filter(role -> role.getRoleName().equalsIgnoreCase(newRoleNameFromRequest))
                        .findFirst();

                if (targetRoleOpt.isPresent()) {
                    accountInDB.setRoleId(targetRoleOpt.get().getId()); // Cập nhật roleId cho đối tượng sẽ được lưu
                    accountInDB.setRoleName(targetRoleOpt.get().getRoleName()); // Cũng cập nhật roleName cho nhất quán
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Vai trò '" + newRoleNameFromRequest + "' không hợp lệ.");
                    response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                    return;
                }
            }

            // 2. Xử lý cập nhật mật khẩu
            // accountFromRequest.getPassword() lấy mật khẩu (dạng text thường) từ JSON
            String plainPasswordFromRequest = accountFromRequest.getPassword();
            if (plainPasswordFromRequest != null && !plainPasswordFromRequest.trim().isEmpty()) {
                // Chỉ cập nhật mật khẩu nếu người dùng nhập mật khẩu mới
                String hashedPassword = BCrypt.hashpw(plainPasswordFromRequest, BCrypt.gensalt());
                accountInDB.setPassword(hashedPassword);
            }
            // Nếu plainPasswordFromRequest rỗng hoặc null, accountInDB.getPassword() (vốn là mật khẩu cũ đã hash) sẽ không bị thay đổi

            // 3. Cập nhật các thông tin khác (ví dụ: cusName, username, email)
            // Giá trị từ JSON đã được Jackson map vào accountFromRequest
            if (accountFromRequest.getCusName() != null) {
                accountInDB.setCusName(accountFromRequest.getCusName());
            }
            if (accountFromRequest.getUsername() != null) {
                accountInDB.setUsername(accountFromRequest.getUsername());
            }
            if (accountFromRequest.getEmail() != null) {
                accountInDB.setEmail(accountFromRequest.getEmail());
            }
            // Các trường khác bạn muốn cho phép cập nhật có thể thêm vào tương tự

            // 4. Gọi service để cập nhật tài khoản với đối tượng accountInDB đã được chỉnh sửa
            boolean isUpdated = accountServices.updateAccount(accountInDB);

            if (isUpdated) {
                request.setAttribute("actionStatus", "success");
                // accountInDB bây giờ là dữ liệu sau khi cập nhật
                request.setAttribute("accountName", accountInDB.getCusName());
                LogUtils.logUpdateAccount(request, beforeUpdateAccount, accountInDB);

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Cập nhật tài khoản thành công!");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Cập nhật tài khoản thất bại! (Có thể không có thay đổi nào được thực hiện)");
            }
        } catch (org.codehaus.jackson.JsonParseException | org.codehaus.jackson.map.JsonMappingException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi định dạng dữ liệu JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi server không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        response.getWriter().write(mapper.writeValueAsString(jsonResponse));
    }
}