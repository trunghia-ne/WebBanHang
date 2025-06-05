package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.AccountDao; // NEW: Import AccountDao
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Role;    // NEW: Import Role
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.utils.LogUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;     // NEW: Import List
import java.util.Optional; // NEW: Import Optional

@WebServlet(name = "AddAccountController", value = "/add-account")
public class AddAccountController extends HttpServlet {
    private static final AccountServices accountServices = new AccountServices();
    private static final AccountDao accountDao = new AccountDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            Account account = gson.fromJson(jsonBuilder.toString(), Account.class);

            String roleNameFromRequest = account.getRole();

            if (roleNameFromRequest == null || roleNameFromRequest.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Vui lòng chọn một vai trò.\"}");
                return;
            }

            List<Role> allRoles = accountDao.getAllRoles();
            Optional<Role> targetRoleOpt = allRoles.stream()
                    .filter(role -> role.getRoleName().equalsIgnoreCase(roleNameFromRequest))
                    .findFirst();

            if (targetRoleOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Vai trò '" + roleNameFromRequest + "' không hợp lệ.\"}");
                return;
            }

            int roleId = targetRoleOpt.get().getId();
            account.setRoleId(roleId);

            boolean success;
            String message;

            success = accountServices.addAccount(account);

            if (success) {
                message = "Thêm tài khoản thành công!";
                request.setAttribute("actionStatus", "success");
                request.setAttribute("username", account.getUsername());
                LogUtils.logAddAccount(request, account);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                message = "Username hoặc Email đã tồn tại! Không thể thêm tài khoản.";
            }

            response.getWriter().write("{\"status\": \"" + (success ? "success" : "error") + "\", \"message\": \"" + message + "\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi thêm tài khoản: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}