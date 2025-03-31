package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.ProductDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "AddProductImgController", value = "/add-product-image")
public class AddProductImgController extends HttpServlet {
    ProductDao productDao = new ProductDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Đọc JSON từ client
            BufferedReader reader = request.getReader();
            String json = reader.lines().collect(Collectors.joining());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);

            int productId = jsonNode.get("productId").asInt();
            String imageUrl = jsonNode.get("url").asText();

            // Lưu vào DB
            productDao.addImage(productId, imageUrl, false);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Lưu ảnh thành công\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Lỗi khi lưu ảnh\"}");
        }
    }
}
