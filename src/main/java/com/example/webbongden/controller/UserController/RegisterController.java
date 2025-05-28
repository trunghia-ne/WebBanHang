package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
// Sử dụng thư viện Jackson mà bạn đã dùng: org.codehaus.jackson
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter; // Nên dùng PrintWriter để đảm bảo encoding
import java.util.List; // Import List để kiểm tra kết quả từ getAccountByUserName
import java.util.Map;

@WebServlet(name = "RegisterController", value = "/register") // Giữ nguyên URL pattern
public class RegisterController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Khởi tạo một lần

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Chuyển hướng tới trang đăng ký như cũ
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter(); // Sử dụng PrintWriter

        try {
            // Đọc dữ liệu JSON từ request
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse JSON thành Map
            Map<String, String> formData = objectMapper.readValue(jsonBuilder.toString(), new TypeReference<Map<String, String>>() {});

            // Lấy thông tin từ formData
            String cusname = formData.get("cusname");
            String username = formData.get("username");
            String email = formData.get("email");
            String password = formData.get("password");
            // String rePassword = formData.get("rePassword"); // rePassword chỉ kiểm tra ở client hoặc nếu cần thì ở đây

            // ------------ KIỂM TRA ĐẦU VÀO CƠ BẢN (NÊN CÓ) ------------
            if (username == null || username.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    cusname == null || cusname.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Vui lòng điền đầy đủ thông tin.\"}");
                out.flush();
                return;
            }
            // (Thêm các validation khác nếu cần: định dạng email, độ dài mật khẩu tối thiểu...)

            // ------------ KIỂM TRA EMAIL VÀ USERNAME TỒN TẠI ------------
            // 1. Kiểm tra email đã tồn tại chưa
            if (accountServices.checkEmailExists(email)) {
                out.print("{\"success\":false,\"message\":\"Email '" + email + "' đã được sử dụng! Vui lòng chọn email khác.\"}");
                out.flush();
                return;
            }

            // 2. Kiểm tra username đã tồn tại chưa
            // AccountServices.getAccountByUserName trả về List<Account>
            List<Account> existingUsernames = accountServices.getAccountByUserName(username);
            if (existingUsernames != null && !existingUsernames.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Tên tài khoản '" + username + "' đã tồn tại! Vui lòng chọn tên khác.\"}");
                out.flush();
                return;
            }

            // ------------ NẾU KIỂM TRA OK, TIẾN HÀNH GỬI OTP ------------
            // Tạo đối tượng Account (mật khẩu vẫn là plain text ở bước này)
            Account accountToRegister = new Account();
            accountToRegister.setCusName(cusname);
            accountToRegister.setUsername(username);
            accountToRegister.setEmail(email);
            accountToRegister.setPassword(password); // Mật khẩu sẽ được hash sau khi OTP được xác thực

            // Lấy hoặc tạo session mới
            HttpSession session = request.getSession(true);

            // Gọi service để bắt đầu quá trình đăng ký OTP
            boolean otpProcessInitiated = accountServices.initiateOtpRegistrationProcess(accountToRegister, session);

            if (otpProcessInitiated) {
                // Thông báo cho client rằng OTP đã được gửi, yêu cầu nhập OTP
                out.print("{\"success\":true,\"message\":\"Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập mã để hoàn tất đăng ký.\"}");
            } else {
                // Lỗi có thể do không gửi được email hoặc lỗi nội bộ khác trong service
                out.print("{\"success\":false,\"message\":\"Đã xảy ra lỗi trong quá trình gửi OTP. Vui lòng thử lại sau.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi ra console server
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Set HTTP status code lỗi
            out.print("{\"success\":false,\"message\":\"Đã xảy ra lỗi không mong muốn trong quá trình xử lý!\"}");
        } finally {
            if (out != null) {
                out.flush(); // Đảm bảo tất cả dữ liệu được gửi đi
                // out.close(); // Không nên close PrintWriter lấy từ response ở đây, container sẽ quản lý
            }
        }
    }
}