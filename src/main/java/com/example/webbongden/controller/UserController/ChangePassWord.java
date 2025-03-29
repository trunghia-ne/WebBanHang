package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ChangePassWord", value = "/changePassword")
public class ChangePassWord extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String username = (String) session.getAttribute("username");

        String message;
        boolean success = false;

        if (username == null) {
            message = "Bạn chưa đăng nhập.";
        } else {
            boolean isChanged = accountServices.changePassword(username, oldPassword, newPassword);
            if (isChanged) {
                success = true;
                message = "Đổi mật khẩu thành công!";
            } else {
                message = "Mật khẩu cũ không chính xác.";
            }
        }

        String json = String.format("{\"success\": %b, \"message\": \"%s\"}", success, message.replace("\"", "\\\""));
        response.getWriter().write(json);
    }
}
