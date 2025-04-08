package com.example.webbongden.filter;

import com.example.webbongden.dao.LogDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Log;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class ActionLoggingFilter implements Filter {
    private final LogDao logDao = new LogDao();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI().toLowerCase();

        // ✅ Đọc session dữ liệu TRƯỚC khi xử lý
        int accountId = 0;
        String role = "guest";
        String action = detectAction(uri);

        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                Object accObj = session.getAttribute("account");
                if (accObj instanceof Account acc) {
                    accountId = acc.getId();
                }

                Object roleAttr = session.getAttribute("role");
                if (roleAttr != null) {
                    role = roleAttr.toString();
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("⚠ Session đã bị invalidate, bỏ qua lấy account/role.");
        }

        // ✅ Cho request xử lý
        chain.doFilter(request, response);

        // ✅ Ghi log nếu cần
        Object status = req.getAttribute("actionStatus");
        boolean isSuccess = "success".equals(status);

        if (isSuccess && action != null && accountId > 0) {
            Log logEntry = new Log();
            logEntry.setAccountId(accountId);
            logEntry.setLevel(role);
            logEntry.setAction(action);
            logEntry.setResource("USER_ACTION");

            Object beforeDataObj = request.getAttribute("beforeData");
            Object afterDataObj = request.getAttribute("afterData");

            logEntry.setBeforeData(beforeDataObj != null ? beforeDataObj.toString() : null);
            logEntry.setAfterData(afterDataObj != null ? afterDataObj.toString() : null);

            try {
                logDao.insertLog(logEntry);
                System.out.println("✅ Logged: " + action);
            } catch (Exception e) {
                System.err.println("❌ Failed to log: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String detectAction(String uri) {
        if (uri == null) return null;
        if (uri.contains("register")) return "USER_REGISTER";
        if (uri.contains("login") && !uri.contains("facebook") && !uri.contains("google")) return "USER_LOGIN";
        if (uri.contains("changepassword")) return "CHANGE_PASSWORD";
        if (uri.contains("update-password")) return "UPDATE_PASSWORD";
        if (uri.contains("edit-cus-info")) return "EDIT_CUS_INFO";
        if (uri.contains("reset-password")) return "RESET_PASSWORD";
        if (uri.contains("paycartcontroller")) return "PAY_CART_CONTROLLER";
        return null;
    }

    public void init(FilterConfig config) {}
    public void destroy() {}
}
