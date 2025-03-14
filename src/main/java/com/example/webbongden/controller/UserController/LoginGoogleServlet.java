package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@WebServlet(name = "LoginGoogleServlet", value = "/login-google")
public class LoginGoogleServlet extends HttpServlet {
    private static final String CLIENT_ID = "625158935097-jbd22lba0t05tm01j6m2bc4n801ncnfj.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/WebBongDen_war/login-google";
    private final AccountDao accountDAO = new AccountDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            System.out.println("Lỗi: Không nhận được mã code từ Google!");
            response.sendRedirect("login.jsp?error=google");
            return;
        }

        try {
            // Gửi mã code lên Google để đổi lấy Access Token và ID Token
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            ).execute();

            // Lấy ID Token từ Access Token
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Lấy thông tin người dùng
            String userId = payload.getSubject();  // ID Google User
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            // Kiểm tra xem tài khoản đã tồn tại chưa
            Optional<Account> existingAccount = accountDAO.findByEmail(email);
            Account account;

            if (existingAccount.isPresent()) {
                account = existingAccount.get();
            } else {
                account = new Account(email, name, userId, userId);
                if (!accountDAO.addAccountUserFB(account)) {
                    request.setAttribute("errorMessage", "Lỗi khi tạo tài khoản. Vui lòng thử lại.");
                    request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                    return;
                }
            }

            // Lưu thông tin vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            session.setAttribute("username", account.getUsername());
            session.setAttribute("role", account.getRole());

            // Điều hướng dựa theo role
            if ("admin".equals(account.getRole())) {
                response.sendRedirect("admin?page=dashboard-management");
            } else {
                response.sendRedirect("home");
            }
        } catch (Exception e) {
            throw new ServletException("Lỗi khi xác thực Google Login", e);
        }
    }
}
