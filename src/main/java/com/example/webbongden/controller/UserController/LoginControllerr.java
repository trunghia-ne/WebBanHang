package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.User;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.UserSevices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoginControllerr", value = "/login")
public class LoginControllerr extends HttpServlet {
    private final AccountServices  accountService = new AccountServices();
    private final UserSevices userSevices = new UserSevices();
    private final OrderSevices orderSevices = new OrderSevices();
    private static final String RECAPTCHA_SECRET_KEY = "6LehGvYqAAAAAB43BbPenYJ5tnrWU3V309hb3O6h";

    private boolean verifyRecaptcha(String captchaResponse) throws IOException {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "secret=" + RECAPTCHA_SECRET_KEY + "&response=" + captchaResponse;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        try (OutputStream out = conn.getOutputStream()) {
            out.write(params.getBytes(StandardCharsets.UTF_8));
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonObject jsonResponse = JsonParser.parseReader(in).getAsJsonObject();
        in.close();

        return jsonResponse.get("success").getAsBoolean();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonObject;
        try (BufferedReader reader = request.getReader()) {
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        }

        String username = jsonObject.get("username").getAsString().trim();
        String password = jsonObject.get("password").getAsString().trim();
        String captcha = jsonObject.has("captcha") ? jsonObject.get("captcha").getAsString() : "";

        if (username.isEmpty() || password.isEmpty()) {
            sendJsonResponse(response, false, "Vui lòng nhập đầy đủ tài khoản và mật khẩu.", null);
            return;
        }

        // Kiểm tra CAPTCHA
        if (!verifyRecaptcha(captcha)) {
            sendJsonResponse(response, false, "Xác thực CAPTCHA không hợp lệ.", null);
            return;
        }

        // Xác thực tài khoản
        Account account = accountService.authenticate(username, password);
        if (account == null) {
            sendJsonResponse(response, false, "Tên đăng nhập hoặc mật khẩu không chính xác.", null);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("account", account);
        session.setAttribute("username", account.getUsername());
        session.setAttribute("role", account.getRole());
        request.setAttribute("actionStatus", "success");
        session.setAttribute("userInfo", userSevices.getBasicInfoByUsername(username));
        session.setAttribute("orders", orderSevices.getOrdersByUsername(username));

        sendJsonResponse(response, true, null, account.getRole());
    }


    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, String role) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", success);
        if (message != null) jsonResponse.put("message", message);
        if (role != null) jsonResponse.put("role", role);
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
}
