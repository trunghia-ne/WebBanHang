package com.example.webbongden.pay;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.VoucherDao;
import com.example.webbongden.dao.model.Invoices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "momoReturn", value = "/pay/momoReturn")
public class MomoReturn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderInfo");
        String resultCode = request.getParameter("resultCode");
        Invoices invoices = new Invoices();
        invoices.setId(Integer.parseInt(orderId));
        HttpSession session = request.getSession();
        if ("0".equals(resultCode)) {
            OrderDao orderDao = new OrderDao();
            orderDao.updateInvoicesStatus(Integer.parseInt(orderId), invoices);
            Object voucherIdObj = session.getAttribute("voucherId");
            int voucherId = (voucherIdObj != null) ? (int) voucherIdObj : 0;
            if (voucherId > 0) {
                try {
                    VoucherDao voucherDao = new VoucherDao();
                    voucherDao.useVoucher(voucherId);
                    session.removeAttribute("voucherId"); // Xóa sau khi dùng để tránh dùng lại
                } catch (Exception e) {
                    e.printStackTrace(); // Có thể log lỗi để xử lý
                }
            }
            request.getSession().setAttribute("transResult", true);
        } else {
            request.getSession().setAttribute("transResult", false);
        }

        response.sendRedirect("/cart#finish");
    }
}
