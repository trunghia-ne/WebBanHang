package com.example.webbongden.pay;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Invoices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "momoReturn", value = "/pay/momoReturn")
public class MomoReturn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        String resultCode = request.getParameter("resultCode");
        Invoices invoices = new Invoices();
        invoices.setId(Integer.parseInt(orderId));
        if ("0".equals(resultCode)) {
            OrderDao orderDao = new OrderDao();
            orderDao.updateInvoicesStatus(Integer.parseInt(orderId), invoices);
            request.getSession().setAttribute("transResult", true);
        } else {
            request.getSession().setAttribute("transResult", false);
        }

        response.sendRedirect("/WebBongDen_war/cart#finish");
    }
}
