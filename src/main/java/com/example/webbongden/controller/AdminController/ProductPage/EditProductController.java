package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.controller.AdminController.AdminNotificationSocket;
import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.utils.LogUtils;
import com.example.webbongden.utils.NotificationUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EditProductController", value ="/edit-product-detail")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class EditProductController extends HttpServlet {
    private static final ProductServices productServices;

    static {
        productServices = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        try {

            // Lấy các tham số từ form
            int id = Integer.parseInt(request.getParameter("id"));
            String productName = request.getParameter("productName");
            double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String productStatus = request.getParameter("productStatus");
            double rating = Double.parseDouble(request.getParameter("rating"));
            String description = request.getParameter("description");
            String warrantyPeriod = request.getParameter("warrantyPeriod");
            String lightColor = request.getParameter("lightColor");
            String material = request.getParameter("material");
            String voltage = request.getParameter("voltage");
            String usageAge = request.getParameter("usageAge");
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));
            int subCategoryId = Integer.parseInt(request.getParameter("subCategoryId"));

            ProductDetail productBefore = productServices.getProductDetailById(id);
            // Tạo đối tượng ProductDetail từ dữ liệu form
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(id);
            productDetail.setProductName(productName);
            productDetail.setUnitPrice(unitPrice);
            productDetail.setStockQuantity(stockQuantity);
            productDetail.setProductStatus(productStatus);
            productDetail.setRating(rating);
            productDetail.setDescription(description);
            productDetail.setWarrantyPeriod(warrantyPeriod);
            productDetail.setLightColor(lightColor);
            productDetail.setMaterial(material);
            productDetail.setVoltage(voltage);
            productDetail.setUsageAge(usageAge);
            productDetail.setDiscountPercent(discountPercent);
            productDetail.setSubCategoryId(subCategoryId);

            // Cập nhật sản phẩm trong cơ sở dữ liệu
            boolean isUpdated = productServices.editProductDetail(productDetail);

            if (isUpdated) {
                request.setAttribute("productId", productDetail.getId());
                request.setAttribute("actionStatus", "success");
                LogUtils.logUpdateProduct(request, productBefore, productDetail);
                response.getWriter().write("{\"success\": true, \"message\": \"Cập nhật sản phẩm thành công!\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể cập nhật sản phẩm!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra trong quá trình cập nhật!\"}");
        }
    }
}
