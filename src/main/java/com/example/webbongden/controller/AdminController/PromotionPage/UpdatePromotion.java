package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.dao.PromotionDao;
import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.utils.LogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdatePromotion", value = "/update-promotion")
public class UpdatePromotion extends HttpServlet {

    private final PromotionDao promotionDao = new PromotionDao();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập response trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc JSON body từ request
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse JSON thành đối tượng Promotion
            Promotion promotion = mapper.readValue(sb.toString(), Promotion.class);

            // Gọi DAO cập nhật
            boolean updated = promotionDao.updatePromotion(promotion);

            if (updated) {
                request.setAttribute("actionStatus", "success");
                request.setAttribute("promotionId", promotion.getId());
                LogUtils.logAddPromotion(request, promotion);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\":true, \"message\":\"Cập nhật thành công.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false, \"message\":\"Cập nhật thất bại.\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false, \"message\":\"Lỗi hệ thống.\"}");
            e.printStackTrace();
        }
    }

    // Nếu muốn hỗ trợ GET (tùy chọn)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("{\"message\":\"Use POST method to update promotion.\"}");
    }
}
