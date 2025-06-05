package com.example.webbongden.controller.listener;

import com.example.webbongden.util.SessionManager;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCleanupListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Không cần làm gì khi session được tạo
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Khi một session bị hủy, lấy username đã lưu trong session
        String username = (String) se.getSession().getAttribute("username");
        if (username != null) {
            // Xóa session này khỏi trình quản lý của chúng ta
            SessionManager.removeUserSession(username);
            System.out.println("Session destroyed for user: " + username + ". Removed from SessionManager.");
        }
    }
}