package com.example.webbongden.controller.AdminController.ProductPage;


import com.example.webbongden.dao.ProductDao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdateProductImg", value = "/update-product-image")
public class UpdateProductImg extends HttpServlet {

    private final ProductDao productImageDAO = new ProductDao();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Đọc JSON từ request body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);

        int imgId = jsonObject.get("imgId").getAsInt();
        String url = jsonObject.get("url").getAsString();
        boolean mainImage = false;

        boolean success = false;
        String message = null;

        try {
            success = productImageDAO.updateProductImageById(imgId, url, mainImage);
            if (!success) {
                message = "Cập nhật ảnh thất bại.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Lỗi server: " + e.getMessage();
        }

        response.setContentType("application/json");
        JsonObject res = new JsonObject();
        res.addProperty("success", success);
        if (!success) {
            res.addProperty("message", message);
        }

        response.getWriter().write(res.toString());
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method not supported");
    }
}
