package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.RatingCount;
import com.example.webbongden.dao.model.Review;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.RevenueServices;
import com.example.webbongden.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ViewDetailController", value = "/product-detail")
public class ViewDetailController extends HttpServlet {
    private static final ProductServices productServices;
    private static final ReviewService reviewService;

    static {
        productServices = new ProductServices();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String productIdParam = request.getParameter("id");
            String pageParam = request.getParameter("page");
            int productId = Integer.parseInt(productIdParam);

            int page = 1;
            int pageSize = 5;

            // Xử lý phân trang
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }

            ProductDetail productDetail = productServices.getProductDetailById(productId);
            List<Review> reviews = reviewService.getReviewsByProductId(productId, page, pageSize);
            int totalReviews = reviewService.getTotalReviewsCount(productId);
            int totalPages = (int) Math.ceil((double) totalReviews / pageSize);
            List<String> listImg = productServices.getAllProductUrls(productId);
            String breadCum = productServices.getCategoryNameByProductId(productId);
            List<Product> relatedProducts = productServices.fetchRelatedProducts(productId);
            List<RatingCount> ratingCounts = reviewService.getRatingCountsByProductId(productId);

            // Tính toán tổng số rating và đánh giá trung bình
            int totalRatings = 0;
            int sumRatings = 0;

            for (RatingCount ratingCount : ratingCounts) {
                totalRatings += ratingCount.getCount();
                sumRatings += ratingCount.getRating() * ratingCount.getCount();
            }

            double averageRating = totalRatings > 0 ? (double) sumRatings / totalRatings : 0.0;
            String formattedRating = String.format("%.1f", averageRating);


            if (productDetail == null) {
                response.sendRedirect("/home?error=product_not_found");
                return;
            }

            // Set product details in the request scope
            request.setAttribute("reviews", reviews);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalReviews", totalReviews);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("listImg", listImg);
            request.setAttribute("breadCum", breadCum);
            request.setAttribute("relatedProducts", relatedProducts);
            request.setAttribute("ratingCounts", ratingCounts);
            request.setAttribute("averageRating", formattedRating);
            request.setAttribute("totalRatings", totalRatings);

            // Forward to the product detail JSP
            request.getRequestDispatcher("/user/product-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/home?error=internal_error");
        }
    }
}
