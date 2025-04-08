package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ResetPasswordController", value = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("reset-email");

        // Kiểm tra email có tồn tại không
        if (!accountServices.checkEmailExists(email)) {
            request.setAttribute("errorMessageReset", "Email không tồn tại.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        }

        // Tạo và gửi OTP
        String otp = accountServices.generateOtp();
        boolean isOtpSent = accountServices.sendOtpToEmail(email, otp);

        if (isOtpSent) {
            // Lưu OTP và email vào session
            HttpSession session = request.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);

            // Đặt attribute để hiển thị form nhập OTP
            request.setAttribute("showOtpForm", true);
            request.setAttribute("actionStatus", true);
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessageReset", "Có lỗi xảy ra khi gửi OTP.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
}
