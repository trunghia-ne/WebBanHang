package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdatePasswordController", value = "/update-password")
public class UpdatePasswordController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPassword = request.getParameter("new-password");
        String confirmPassword = request.getParameter("confirm-password");
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        // Kiểm tra mật khẩu mới
        if (newPassword == null || newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessageReset", "Mật khẩu mới không hợp lệ.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu mới
        boolean isPasswordUpdated = accountServices.updatePassword(email, newPassword);
        if (isPasswordUpdated) {
            request.setAttribute("successMessageReset", "Mật khẩu đã được cập nhật thành công.");
            session.removeAttribute("otp"); // Xóa OTP khỏi session
            session.removeAttribute("email"); // Xóa email khỏi session
        } else {
            request.setAttribute("errorMessageReset", "Có lỗi xảy ra khi cập nhật mật khẩu.");
        }

        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }
}
