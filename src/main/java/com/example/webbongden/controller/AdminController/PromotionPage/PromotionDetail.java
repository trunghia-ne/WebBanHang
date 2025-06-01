package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.dao.PromotionDao;
import com.example.webbongden.dao.model.Promotion;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "PromotionDetail", value = "/get-promotion-detail")
public class PromotionDetail extends HttpServlet {

    private final PromotionDao promotionDao = new PromotionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy promotionId từ tham số query string
        String promotionIdStr = request.getParameter("promotionId");
        if (promotionIdStr == null || promotionIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing promotionId\"}");
            return;
        }

        int promotionId;
        try {
            promotionId = Integer.parseInt(promotionIdStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid promotionId\"}");
            return;
        }

        Promotion promotion = promotionDao.getPromotionDetailById(promotionId);
        if (promotion == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Promotion not found\"}");
            return;
        }

        // Trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(promotion);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Bạn có thể để trống hoặc chuyển sang doGet tùy ý
        doGet(request, response);
    }
}

