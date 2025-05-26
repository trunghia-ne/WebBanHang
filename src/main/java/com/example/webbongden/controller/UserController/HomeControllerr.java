package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeControllerr", value = "/home")
public class HomeControllerr extends HttpServlet {
    private static final PromotionService promotionService = new PromotionService();
    private static final ProductServices productServices = new ProductServices();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Promotion> listPromotion = promotionService.getAllPromotionsWithProducts();
        List<Product> listHotProduct = productServices.getBestSellingProducts();
        request.setAttribute("listPromotion", listPromotion);
        request.setAttribute("listHotProduct", listHotProduct);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
