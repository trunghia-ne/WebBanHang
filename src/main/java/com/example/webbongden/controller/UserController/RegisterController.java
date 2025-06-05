package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RegisterController", value = "/register")
public class RegisterController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Map<String, String> formData = objectMapper.readValue(jsonBuilder.toString(), new TypeReference<Map<String, String>>() {
            });

            String cusname = formData.get("cusname");
            String username = formData.get("username");
            String email = formData.get("email");
            String password = formData.get("password");
            if (username == null || username.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    cusname == null || cusname.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Vui lòng điền đầy đủ thông tin.\"}");
                out.flush();
                return;
            }
            if (accountServices.checkEmailExists(email)) {
                out.print("{\"success\":false,\"message\":\"Email '" + email + "' đã được sử dụng! Vui lòng chọn email khác.\"}");
                out.flush();
                return;
            }

            List<Account> existingUsernames = accountServices.getAccountByUserName(username);
            if (existingUsernames != null && !existingUsernames.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Tên tài khoản '" + username + "' đã tồn tại! Vui lòng chọn tên khác.\"}");
                out.flush();
                return;
            }

            Account accountToRegister = new Account();
            accountToRegister.setCusName(cusname);
            accountToRegister.setUsername(username);
            accountToRegister.setEmail(email);
            accountToRegister.setPassword(password);

            HttpSession session = request.getSession(true);

            boolean otpProcessInitiated = accountServices.initiateOtpRegistrationProcess(accountToRegister, session);

            if (otpProcessInitiated) {
                out.print("{\"success\":true,\"message\":\"Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập mã để hoàn tất đăng ký.\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Đã xảy ra lỗi trong quá trình gửi OTP. Vui lòng thử lại sau.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\":false,\"message\":\"Đã xảy ra lỗi không mong muốn trong quá trình xử lý!\"}");
        } finally {
            if (out != null) {
                out.flush();
            }
        }
    }
}