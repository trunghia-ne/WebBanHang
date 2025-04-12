package com.example.webbongden.controller.AdminController.LogPage;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "SearchLog", value = "/search-log")
public class SearchLog extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logId = request.getParameter("logId");
        request.setAttribute("logId", logId);
        request.getRequestDispatcher("/admin/log.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
