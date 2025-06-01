package com.example.webbongden.controller.AdminController.VoucherPage;

import com.example.webbongden.dao.VoucherDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteVoucherController ", value = "/DeleteVoucherController")
public class DeleteVoucherController extends HttpServlet {
    VoucherDao voucherDao = new VoucherDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            voucherDao.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi khi xóa voucher: " + e.getMessage());
        }
    }
}