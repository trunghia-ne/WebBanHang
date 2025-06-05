package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.OrderSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/cancel-order")
public class CancelOrder extends HttpServlet {

    // Tạo đối tượng OrderDao để tương tác với cơ sở dữ liệu
    private OrderSevices orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = new OrderSevices();  // Khởi tạo OrderDao
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy orderId từ request
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        // Kiểm tra trạng thái đơn hàng trước khi hủy
        String orderStatus = orderDao.getOrderStatus(orderId);

        if ("Pending".equals(orderStatus)) {
            // Nếu đơn hàng đang trong trạng thái Pending, tiến hành hủy đơn hàng
            boolean isCanceled = orderDao.updateOrderStatus(orderId, "Canceled");

            // Trả về kết quả cho frontend
            response.setContentType("application/json");
            if (isCanceled) {
                response.getWriter().write("{\"success\": true}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể hủy đơn hàng.\"}");
            }
        } else {
            // Nếu đơn hàng không phải "Pending", không thể hủy
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Đơn hàng không thể hủy vì đã được phê duyệt.\"}");
        }
    }
}

