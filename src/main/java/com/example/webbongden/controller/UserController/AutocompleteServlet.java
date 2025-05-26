package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.ProductDao;
import com.example.webbongden.dao.model.Product;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AutocompleteServlet", value = "/autocomplete")
public class AutocompleteServlet extends HttpServlet {
    private ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Product> products = productDao.searchProductsByName(query);

        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(products);
        response.getWriter().write(json);
    }
}
