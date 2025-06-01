package com.example.webbongden.dao.model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Set;

public class SecurityUtils {

    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("account") != null;
    }

    public static boolean hasPermission(HttpServletRequest request, String requiredPermission) {
        if (!isAuthenticated(request)) {
            return false;
        }

        HttpSession session = request.getSession(false);

        @SuppressWarnings("unchecked")
        Set<String> userPermissions = (Set<String>) session.getAttribute("userPermissions");

        if (userPermissions == null) {
            return false;
        }

        return userPermissions.contains(requiredPermission);
    }

    public static Account getAuthAccount(HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return null;
        }
        HttpSession session = request.getSession(false);
        return (Account) session.getAttribute("account");
    }
}