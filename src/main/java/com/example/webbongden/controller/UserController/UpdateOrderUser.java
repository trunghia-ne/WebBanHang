package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateOrderUser", value = "/update-order-user")
public class UpdateOrderUser extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));  // Lấy orderId từ form hoặc URL
        String shippingAddress = request.getParameter("shippingAddress");  // Lấy địa chỉ từ form
        String phoneNumber = request.getParameter("phoneNumber");  // Lấy số điện thoại từ form

        // Gọi phương thức updateOrder trong OrderDao để cập nhật thông tin đơn hàng
        boolean isUpdated = orderDao.updateOrder(orderId, shippingAddress, phoneNumber);

        // Cấu hình phản hồi trả về dưới dạng JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (isUpdated) {
            // Trả về kết quả thành công
            out.write("{\"success\": true}");
        } else {
            // Trả về kết quả thất bại
            out.write("{\"success\": false}");
        }
        out.flush();
    }
}

