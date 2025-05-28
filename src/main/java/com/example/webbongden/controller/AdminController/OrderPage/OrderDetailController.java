package com.example.webbongden.controller.AdminController.OrderPage;

import com.example.webbongden.dao.model.OrderDetail;
import com.example.webbongden.dao.model.OrderDetailResponse;
import com.example.webbongden.services.OrderSevices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailController", value = "/order-detail")
public class OrderDetailController extends HttpServlet {
    private static final OrderSevices orderServices;

    static {
        orderServices = new OrderSevices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing orderId parameter\"}");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            List<OrderDetail> orderDetails = orderServices.getOrderDetailsById(orderId);
            double shippingFee = orderServices.getShippingFeeById(orderId); // THÊM HÀM NÀY TRONG service

            OrderDetailResponse orderDetailResponse = new OrderDetailResponse(orderDetails, shippingFee);

            String jsonResponse = new Gson().toJson(orderDetailResponse);
            response.getWriter().write(jsonResponse);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid orderId format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Unable to fetch order details\"}");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
