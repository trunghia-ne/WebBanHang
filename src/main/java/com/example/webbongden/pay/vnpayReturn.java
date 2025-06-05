package com.example.webbongden.pay;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.VoucherDao;
import com.example.webbongden.dao.model.Invoices;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
@WebServlet(name = "vnpayReturn", value = "/pay/vnpayReturn")
public class vnpayReturn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("aaaa");
        try ( PrintWriter out = response.getWriter()) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");

                String orderId = request.getParameter("vnp_TxnRef");
                System.out.println(orderId);

                System.out.println("Generated sign: " + signValue);
                System.out.println("Provided sign: " + vnp_SecureHash);

                Invoices invoices = new Invoices();
                int invoiceId = Integer.parseInt(orderId.split("-")[0]);
                invoices.setId(invoiceId);
                HttpSession session = request.getSession();
                boolean transSuccess = false;
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    //update banking system
                    invoices.setPaymentStatus("Completed");
                    transSuccess = true;

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
                } else {
                    invoices.setPaymentStatus("Pending");
                }
                System.out.println("TransResult: " + transSuccess);
                OrderDao orderDao = new OrderDao();
                orderDao.updateInvoicesStatus(invoices.getId(), invoices);
                session.setAttribute("transResult", transSuccess);
                response.sendRedirect("/cart#finish");
            } else {
                //RETURN PAGE ERROR
                System.out.println("GD KO HOP LE (invalid signature)");
            }
        }
    }
}