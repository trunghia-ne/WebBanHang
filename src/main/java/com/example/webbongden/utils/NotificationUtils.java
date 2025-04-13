package com.example.webbongden.utils;

import com.example.webbongden.controller.AdminController.AdminNotificationSocket;
import com.example.webbongden.dao.NotiDao;
import com.example.webbongden.dao.model.Notifications;

public class NotificationUtils {
    public static void notifyAllAdmins(String message, String link) {
        NotiDao dao = new NotiDao();
        dao.insertNotification(new Notifications(null, message, link));

        // Gửi đúng JSON để client parse được
        String json = String.format("{\"message\":\"%s\", \"link\":\"%s\"}", escapeJson(message), escapeJson(link));
        AdminNotificationSocket.broadcast(json);
    }

    private static String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
    }
}
