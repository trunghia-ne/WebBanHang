package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

@WebServlet(name = "LoginFacebookServlet", value = "/login-facebook")
public class LoginFacebookServlet extends HttpServlet {
    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=";
    private final AccountDao accountDAO = new AccountDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = request.getParameter("access_token");

        if (accessToken == null || accessToken.isEmpty()) {
            response.sendRedirect("login.jsp?error=invalid_token");
            return;
        }

        // Lấy thông tin user từ Facebook API
        String jsonResponse = getUserInfoFromFacebook(accessToken);
        if (jsonResponse == null) {
            response.sendRedirect("login.jsp?error=facebook_api_error");
            return;
        }

        // Parse JSON để lấy thông tin user
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userNode = objectMapper.readTree(jsonResponse);
        String facebookId = userNode.get("id").asText();
        String name = userNode.get("name").asText();
        String email = userNode.has("email") ? userNode.get("email").asText() : "No Email";

        // Kiểm tra xem email đã tồn tại trong database chưa
        Optional<Account> existingAccount = accountDAO.findByEmail(email);
        Account account;

        if (existingAccount.isPresent()) {
            // Nếu đã tồn tại, sử dụng tài khoản cũ
            account = existingAccount.get();
        } else {
            // Nếu chưa tồn tại, tạo tài khoản mới
            account = new Account(email, name, facebookId, facebookId);
            if (!accountDAO.addAccountUserFB(account)) {
                request.setAttribute("errorMessage", "Lỗi khi tạo tài khoản. Vui lòng thử lại.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                return;
            }
        }

        // Lưu tài khoản vào session
        HttpSession session = request.getSession();
        session.setAttribute("account", account);
        session.setAttribute("username", account.getUsername());
        session.setAttribute("role", account.getRole());

        // Điều hướng đến trang phù hợp
        if ("admin".equals(account.getRole())) {
            response.sendRedirect("admin?page=dashboard-management");
        } else {
            response.sendRedirect("home");
        }
    }

    private String getUserInfoFromFacebook(String accessToken) {
        try {
            URL url = new URL(FACEBOOK_GRAPH_API_URL + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
