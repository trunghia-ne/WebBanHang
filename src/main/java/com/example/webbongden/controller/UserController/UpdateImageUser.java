package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.AccountDao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdateImageUser", value = "/update-avatar")
public class UpdateImageUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        try {
            // Đọc body JSON từ request
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            // Parse JSON
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);

            int customerId = json.get("customerId").getAsInt();
            String avatarUrl = json.get("avatarUrl").getAsString();

            // Cập nhật avatar vào DB
            AccountDao dao = new AccountDao();
            dao.updateAvatar(customerId, avatarUrl); // ⚠️ Đảm bảo phương thức này có sẵn trong DAO

            // Trả kết quả thành công
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi cập nhật avatar\"}");
        } finally {
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
