package com.example.webbongden.utils;

import com.example.webbongden.controller.AdminController.AdminNotificationSocket;
import com.example.webbongden.dao.NotiDao;
import com.example.webbongden.dao.model.Notifications;

public class NotificationUtils {
    public static void notifyAllAdmins(String message, String link) {
        NotiDao dao = new NotiDao();
        dao.insertNotification(new Notifications(null, message, link));
        AdminNotificationSocket.broadcast(message);
    }
}
