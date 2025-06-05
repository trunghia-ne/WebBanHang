package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Role;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.util.SessionManager; // <<< THÊM #1: Import SessionManager
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
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "UpdateAccount", value = "/update-account")
public class UpdateAccount extends HttpServlet {
    private static final AccountServices accountServices;
    private static final AccountDao accountDao;

    static {
        accountServices = new AccountServices();
        accountDao = new AccountDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ... (phần này được giữ nguyên, không thay đổi)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
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

            Map<String, Object> accountData = new HashMap<>();
            accountData.put("id", account.getId());
            accountData.put("cusName", account.getCusName());
            accountData.put("username", account.getUsername());
            accountData.put("email", account.getEmail());
            accountData.put("role", account.getRoleName());
            accountData.put("createdAt", account.getCreatedAt() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(account.getCreatedAt()) : "");

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
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Account accountFromRequest = mapper.readValue(jsonBuilder.toString(), Account.class);

            Account accountInDB = accountServices.getAccountById(accountFromRequest.getId());
            if (accountInDB == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không tìm thấy tài khoản để cập nhật (ID: " + accountFromRequest.getId() + ").");
                response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                return;
            }

            Account beforeUpdateAccount = accountServices.getAccountById(accountFromRequest.getId());

            // <<< THÊM #2: Logic để xác định quyền có bị thay đổi hay không
            boolean roleWasChanged = false;
            String newRoleNameFromRequest = accountFromRequest.getRole();
            // So sánh vai trò mới từ request với vai trò cũ trong DB (trước khi cập nhật)
            if (newRoleNameFromRequest != null && !newRoleNameFromRequest.trim().isEmpty() && !newRoleNameFromRequest.equalsIgnoreCase(beforeUpdateAccount.getRoleName())) {
                roleWasChanged = true;
            }
            // --- Kết thúc phần thêm #2 ---


            // 1. Xử lý cập nhật vai trò (Role)
            if (newRoleNameFromRequest != null && !newRoleNameFromRequest.trim().isEmpty()) {
                List<Role> allRoles = accountDao.getAllRoles();
                Optional<Role> targetRoleOpt = allRoles.stream()
                        .filter(role -> role.getRoleName().equalsIgnoreCase(newRoleNameFromRequest))
                        .findFirst();

                if (targetRoleOpt.isPresent()) {
                    accountInDB.setRoleId(targetRoleOpt.get().getId());
                    accountInDB.setRoleName(targetRoleOpt.get().getRoleName());
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Vai trò '" + newRoleNameFromRequest + "' không hợp lệ.");
                    response.getWriter().write(mapper.writeValueAsString(jsonResponse));
                    return;
                }
            }

            // 2. Xử lý cập nhật mật khẩu
            String plainPasswordFromRequest = accountFromRequest.getPassword();
            if (plainPasswordFromRequest != null && !plainPasswordFromRequest.trim().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(plainPasswordFromRequest, BCrypt.gensalt());
                accountInDB.setPassword(hashedPassword);
            }

            // 3. Cập nhật các thông tin khác
            if (accountFromRequest.getCusName() != null) {
                accountInDB.setCusName(accountFromRequest.getCusName());
            }
            if (accountFromRequest.getUsername() != null) {
                accountInDB.setUsername(accountFromRequest.getUsername());
            }
            if (accountFromRequest.getEmail() != null) {
                accountInDB.setEmail(accountFromRequest.getEmail());
            }

            // 4. Gọi service để cập nhật tài khoản
            boolean isUpdated = accountServices.updateAccount(accountInDB);

            if (isUpdated) {
                request.setAttribute("actionStatus", "success");
                request.setAttribute("accountName", accountInDB.getCusName());
                LogUtils.logUpdateAccount(request, beforeUpdateAccount, accountInDB);

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Cập nhật tài khoản thành công!");

                // <<< THÊM #3: Gọi cắt session nếu quyền đã thay đổi
                if (roleWasChanged) {
                    System.out.println("Role changed for user: " + accountInDB.getUsername() + ". Invalidating session.");
                    SessionManager.invalidateUserSession(accountInDB.getUsername());
                }
                // --- Kết thúc phần thêm #3 ---

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