package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.UserSevices;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@WebServlet(name = "LoginGoogleServlet", value = "/login-google")
public class LoginGoogleServlet extends HttpServlet {
    private static final String CLIENT_ID = "625158935097-jbd22lba0t05tm01j6m2bc4n801ncnfj.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/WebBongDen_war/login-google";

    private final AccountDao accountDAO = new AccountDao();
    private final UserSevices userSevices = new UserSevices();
    private final OrderSevices orderSevices = new OrderSevices();
    private final AccountServices accountService = new AccountServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            System.out.println("Lỗi: Không nhận được mã code từ Google!");
            response.sendRedirect(request.getContextPath() + "/login?error=google_auth_failed");
            return;
        }

        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String usernameForNewAccount = email;

            Optional<Account> existingAccountOpt = accountDAO.findByEmail(email);
            Account account;

            if (existingAccountOpt.isPresent()) {
                account = existingAccountOpt.get();
            } else {
                account = new Account(email, name, pictureUrl, usernameForNewAccount, "G00gl3P@sswOrd");

                if (!accountDAO.addAccountUserFB(account)) {
                    request.setAttribute("errorMessage", "Lỗi khi tạo tài khoản Google. Vui lòng thử lại.");
                    request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                    return;
                }
                account.setRoleId(1);
                account.setRoleName("customer");
            }

            HttpSession session = request.getSession();

            session.setAttribute("account", account);

            Set<String> permissions = accountService.getPermissionsByRoleId(account.getRoleId());
            session.setAttribute("userPermissions", permissions);
            session.setAttribute("username", account.getUsername());
            if (pictureUrl != null && !pictureUrl.isEmpty()) {
                session.setAttribute("avatar", pictureUrl);
            }
            if ("admin".equalsIgnoreCase(account.getRoleName())) {
                response.sendRedirect(request.getContextPath() + "/admin?page=dashboard-management");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (Exception e) {
            System.err.println("Lỗi nghiêm trọng khi xác thực Google Login: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=google_processing_failed");
        }
    }
}