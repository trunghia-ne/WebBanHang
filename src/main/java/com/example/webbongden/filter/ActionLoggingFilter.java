package com.example.webbongden.filter;

import com.example.webbongden.dao.LogDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Log;
import com.example.webbongden.utils.NotificationUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class ActionLoggingFilter implements Filter {
    private final LogDao logDao = new LogDao();
    private boolean isNotificationSent = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI().toLowerCase();

        int accountId = 0;
        String roleName = "guest";
        String action = detectAction(uri);

        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                Object accObj = session.getAttribute("account");
                if (accObj instanceof Account acc) {
                    accountId = acc.getId();
                    roleName = acc.getRoleName();
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("⚠ Session đã bị invalidate, bỏ qua lấy account/role.");
        }

        chain.doFilter(request, response);

        Object status = req.getAttribute("actionStatus");
        boolean isSuccess = "success".equals(status);

        if (isSuccess && action != null && accountId > 0) {
            Log logEntry = new Log();
            logEntry.setAccountId(accountId);
            logEntry.setLevel(roleName);
            logEntry.setAction(action);
            logEntry.setResource("USER_ACTION");

            // Ghi lại dữ liệu trước và sau khi cập nhật
            Object beforeDataObj = request.getAttribute("beforeData");
            Object afterDataObj = request.getAttribute("afterData");

            logEntry.setBeforeData(beforeDataObj != null ? beforeDataObj.toString() : null);
            logEntry.setAfterData(afterDataObj != null ? afterDataObj.toString() : null);

            try {
                int logId = logDao.insertLog(logEntry);
                String message = buildNotificationMessage(action, req);

                // Kiểm tra nếu chưa gửi thông báo
                if (!isNotificationSent) {
                    NotificationUtils.notifyAllAdmins(message, "/search-log?logId=" + logId);
                    isNotificationSent = true;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to log: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Phương thức phát hiện action dựa trên URI
    private String detectAction(String uri) {
        if (uri == null) return null;
        if (uri.contains("register")) return "USER_REGISTER";
        if (uri.contains("login") && !uri.contains("facebook") && !uri.contains("google")) return "USER_LOGIN";
        if (uri.contains("change-password")) return "CHANGE_PASSWORD";
        if (uri.contains("update-password")) return "UPDATE_PASSWORD";
        if (uri.contains("edit-cus-info")) return "EDIT_CUS_INFO";
        if (uri.contains("reset-password")) return "RESET_PASSWORD";
        if (uri.contains("paycartcontroller")) return "PAY_CART_CONTROLLER";
        if (uri.contains("update-order-user")) return "UPDATE_ORDER_USER";  // Thêm action này để nhận diện
        return null;
    }

    // Xây dựng thông điệp thông báo cho action
    private String buildNotificationMessage(String action, HttpServletRequest req) {
        String verb;
        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");
        if (action.startsWith("PAY_CART")) {
            verb = "Nhận được đơn hàng mới ";
        } else if(action.startsWith("UPDATE_ORDER_USER")) {
            verb = " vừa thay đổi thông tin vận chuyển";  // Thêm hành động thay đổi thông tin đơn hàng
        } else {
            verb = "thực hiện hành động";
        }

        String detail = "";

        switch (action) {
            case "PAY_CART_CONTROLLER":
                Object orderIdObj = req.getAttribute("orderId");
                if (orderIdObj != null) {
                    detail = "Nhận được đơn hàng mới - Mã đơn hàng: " + orderIdObj.toString();
                } else {
                    detail = " - Không xác định mã đơn hàng";
                }
                break;
            case "UPDATE_ORDER_USER":
                // Xử lý thông báo khi cập nhật thông tin đơn hàng
                detail = "Khách hàng " + username + verb;
                break;
        }

        return detail;
    }
}
