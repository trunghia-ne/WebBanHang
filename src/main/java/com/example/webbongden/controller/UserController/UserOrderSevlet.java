package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserOrderSevlet", value = "/user-orders")
public class UserOrderSevlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Lấy userId từ tham số request (ví dụ: /user-orders?userId=123)
        String userIdParam = request.getParameter("userId");

        if (userIdParam == null) {
            // Thiếu tham số userId -> trả lỗi
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing userId parameter\"}");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid userId format\"}");
            return;
        }

        // Lấy danh sách đơn hàng từ database qua DAO theo userId
        List<Order> orders = orderDao.getListOrdersByUserId(userId);

        // Chuyển list sang JSON
        Gson gson = new Gson();
        String json = gson.toJson(orders);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
