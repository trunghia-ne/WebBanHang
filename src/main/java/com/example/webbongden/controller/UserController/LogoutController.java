package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.LogDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Log;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutController", value = "/LogoutController")
public class LogoutController extends HttpServlet {
    private final LogDao logDao = new LogDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int accountId = 0;
        String role = "guest";

        if (session != null) {
            try {
                Account account = (Account) session.getAttribute("account");
                if (account != null) {
                    accountId = account.getId();
                }
                Object roleAttr = session.getAttribute("role");
                if (roleAttr != null) {
                    role = roleAttr.toString();
                }

                // ✅ Ghi log TRƯỚC khi invalidate session
                Log logEntry = new Log();
                logEntry.setAccountId(accountId);
                logEntry.setLevel(role);
                logEntry.setAction("USER_LOGOUT");
                logEntry.setResource("USER_ACTION");
                logEntry.setBeforeData(null);
                logEntry.setAfterData(null);

                logDao.insertLog(logEntry);
                System.out.println("✅ Logout logged: " + accountId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            session.invalidate();
        }

        response.sendRedirect("home");
    }
}

