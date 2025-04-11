package com.example.webbongden.controller.AdminController;

import com.example.webbongden.dao.NotiDao;
import com.example.webbongden.dao.model.Notifications;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.RevenueServices;
import com.example.webbongden.services.UserSevices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminLoadNoti", value = "/admin/load-notifications")
public class AdminLoadNoti extends HttpServlet {
    private static final NotiDao dao;

    static {
       dao = new NotiDao();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<Notifications> notis = dao.getNotificationsForAdmin(null);
        new Gson().toJson(notis, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
