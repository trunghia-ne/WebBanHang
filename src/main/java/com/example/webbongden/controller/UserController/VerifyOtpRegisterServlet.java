package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
// Import lớp RegistrationResult từ AccountServices
import com.example.webbongden.services.AccountServices.RegistrationResult;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
// Sử dụng thư viện Jackson để tạo JSON response, giống như RegisterController
import org.codehaus.jackson.map.ObjectMapper; // Hoặc com.fasterxml.jackson.databind.ObjectMapper nếu bạn dùng Jackson 2.x

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap; // Có thể dùng HashMap để xây dựng JSON response
import java.util.Map;

@WebServlet(name = "VerifyOtpRegisterServlet", value = "/VerifyOtpRegisterServlet") // URL mapping của bạn
public class VerifyOtpRegisterServlet extends HttpServlet {

    private AccountServices accountServices;
    private ObjectMapper objectMapper; // Để tạo JSON response

    @Override
    public void init() throws ServletException {
        super.init();
        accountServices = new AccountServices();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thông thường, việc xác thực OTP không nên qua GET.
        // Có thể chuyển hướng người dùng về trang đăng ký hoặc hiển thị thông báo lỗi.
        response.sendRedirect(request.getContextPath() + "/register"); // Ví dụ: chuyển về trang đăng ký
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Lấy mã OTP từ request. Giả sử client gửi OTP qua parameter tên là "otp".
        // Trong JavaScript của bạn, khi submit form OTP, đảm bảo tên trường là "otp".
        String userSubmittedOtp = request.getParameter("otp");

        // Lấy session hiện tại, không tạo mới nếu chưa có.
        // Nếu không có session, nghĩa là quá trình đăng ký chưa được bắt đầu đúng cách.
        HttpSession session = request.getSession(false);

        Map<String, Object> jsonResponse = new HashMap<>();

        if (session == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Phiên làm việc không hợp lệ hoặc đã hết hạn. Vui lòng thử đăng ký lại.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }

        if (userSubmittedOtp == null || userSubmittedOtp.trim().isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Vui lòng nhập mã OTP.");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
            return;
        }

        try {
            // Gọi service để xác thực OTP và hoàn tất đăng ký
            RegistrationResult registrationResult = accountServices.finalizeOtpRegistration(userSubmittedOtp.trim(), session);

            if (registrationResult.isSuccess()) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", registrationResult.getMessage());
                // Bạn có thể thêm URL chuyển hướng vào response nếu muốn client tự động chuyển trang
                jsonResponse.put("redirectUrl", request.getContextPath() + "/login"); // Ví dụ: chuyển đến trang đăng nhập
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", registrationResult.getMessage());
            }
            out.print(objectMapper.writeValueAsString(jsonResponse));

        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Đã xảy ra lỗi không mong muốn trong quá trình xác thực OTP.");
            // Không nên gửi chi tiết lỗi e.getMessage() cho client trong môi trường production
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(objectMapper.writeValueAsString(jsonResponse));
        } finally {
            if (out != null) {
                out.flush();
            }
        }
    }
}