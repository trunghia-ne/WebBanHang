package com.example.webbongden.util;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Sử dụng ConcurrentHashMap để đảm bảo an toàn trong môi trường đa luồng
    private static final Map<String, HttpSession> activeUserSessions = new ConcurrentHashMap<>();

    /**
     * Thêm một session của người dùng vào danh sách quản lý.
     * @param username Tên đăng nhập của người dùng.
     * @param session Session của người dùng đó.
     */
    public static void addUserSession(String username, HttpSession session) {
        if (username != null && session != null) {
            activeUserSessions.put(username, session);
        }
    }

    /**
     * Xóa một session của người dùng khỏi danh sách quản lý.
     * @param username Tên đăng nhập của người dùng.
     */
    public static void removeUserSession(String username) {
        if (username != null) {
            activeUserSessions.remove(username);
        }
    }

    /**
     * Vô hiệu hóa (invalidate) session của một người dùng cụ thể.
     * @param username Tên đăng nhập của người dùng cần vô hiệu hóa session.
     */
    public static void invalidateUserSession(String username) {
        if (username != null && activeUserSessions.containsKey(username)) {
            HttpSession session = activeUserSessions.get(username);
            if (session != null) {
                try {
                    session.invalidate(); // Hủy session
                } catch (IllegalStateException e) {
                    // Session có thể đã bị hủy bởi một tiến trình khác (ví dụ: timeout)
                    // Không cần làm gì thêm, chỉ cần đảm bảo nó được xóa khỏi map
                    System.out.println("Session for " + username + " was already invalidated.");
                } finally {
                    // Luôn xóa khỏi map sau khi đã xử lý
                    activeUserSessions.remove(username);
                }
            }
        }
    }
}