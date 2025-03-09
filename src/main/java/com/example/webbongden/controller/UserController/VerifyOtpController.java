package com.example.webbongden.controller.UserController;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerifyOtpController", value = "/verify-otp")
public class VerifyOtpController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userOtp = request.getParameter("otp");
        HttpSession session = request.getSession();
        String storedOtp = (String) session.getAttribute("otp");

        // Kiểm tra OTP
        if (userOtp != null && userOtp.equals(storedOtp)) {
            // Đặt attribute để hiển thị form nhập mật khẩu mới
            request.setAttribute("showNewPasswordForm", true);
            request.setAttribute("successMessageReset", "OTP hợp lệ. Vui lòng nhập mật khẩu mới.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessageReset", "OTP không hợp lệ.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
}
