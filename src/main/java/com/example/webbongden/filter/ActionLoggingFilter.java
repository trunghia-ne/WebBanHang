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
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI().toLowerCase();
        chain.doFilter(request, response);

        Account account = (session != null) ? (Account) session.getAttribute("account") : null;
        String role = (session != null && session.getAttribute("role") != null)
                ? (String) session.getAttribute("role") : "guest";
        int accountId = (account != null) ? account.getId() : 0;
        Object status = req.getAttribute("actionStatus");
        boolean isSuccess = "success".equals(status);
        // Tự phát hiện hành động
        String action = detectAction(uri);
        if (isSuccess) {
            Log logEntry = new Log();
            logEntry.setAccountId(accountId);
            logEntry.setLevel(role);
            logEntry.setAction(action);
            logEntry.setResource("USER_ACTION");
            logEntry.setBeforeData(null);
            logEntry.setAfterData(null);

            try {
                logDao.insertLog(logEntry);
                System.out.println("✅ User action logged: " + action);
            } catch (Exception e) {
                System.err.println("❌ Failed to log user action: " + e.getMessage());
            }
        }
    }

    private String detectAction(String uri) {
        if (uri.contains("add-to-cart")) return "ADD_TO_CART";
        if (uri.contains("delete-cart-item")) return "DELETE_FROM_CART";
        if (uri.contains("pay-cart")) return "CHECKOUT";
        if (uri.contains("register")) return "USER_REGISTER";
        if (uri.contains("login") && !uri.contains("facebook") && !uri.contains("google")) return "USER_LOGIN";
        if (uri.contains("logout")) return "USER_LOGOUT";
        if (uri.contains("update-cart")) return "UPDATE_CART_ITEM";
        if (uri.contains("change-password")) return "CHANGE_PASSWORD";
        return null;
    }

    public void init(FilterConfig config) {}
    public void destroy() {}
}
