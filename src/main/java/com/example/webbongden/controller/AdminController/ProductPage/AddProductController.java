package com.example.webbongden.controller.AdminController.ProductPage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.utils.CloudinaryConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminAddProductController", value = "/add-product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class AddProductController extends HttpServlet {
    private final ProductServices productServices = new ProductServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến form thêm sản phẩm
        request.getRequestDispatcher("admin/product-management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy thông tin sản phẩm
            String productName = request.getParameter("productName");
            String subCategoryName = request.getParameter("categoryName");
            double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String productStatus = request.getParameter("productStatus");
            String description = request.getParameter("description");
            String warrantyPeriod = request.getParameter("warrantyPeriod");
            String lightColor = request.getParameter("lightColor");
            String material = request.getParameter("material");
            String voltage = request.getParameter("voltage");
            String usageAge = request.getParameter("usageAge");
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));

            ProductDetail product = new ProductDetail();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setStockQuantity(stockQuantity);
            product.setProductStatus(productStatus);
            product.setDescription(description);
            product.setWarrantyPeriod(warrantyPeriod);
            product.setLightColor(lightColor);
            product.setMaterial(material);
            product.setVoltage(voltage);
            product.setUsageAge(usageAge);
            product.setDiscountPercent(discountPercent);
            product.setCreatedAt(new Date());

            // Upload ảnh lên Cloudinary
            List<ProductImage> images = new ArrayList<>();
            Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

            for (Part part : request.getParts()) {
                if (part.getName().equals("productImages") && part.getSize() > 0) {
                    byte[] imageBytes = part.getInputStream().readAllBytes();
                    Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());

                    String imageUrl = (String) uploadResult.get("secure_url");

                    ProductImage image = new ProductImage();
                    image.setUrl(imageUrl);
                    image.setMainImage(images.isEmpty()); // ảnh đầu là ảnh chính
                    images.add(image);
                }
            }
            product.setListImages(images);

            // Gọi service để lưu sản phẩm
            boolean isAdded = productServices.addProduct(product, subCategoryName);

            if (isAdded) {
                request.setAttribute("actionStatus", "success");
                request.setAttribute("message", "Thêm sản phẩm thành công!");
            } else {
                request.setAttribute("error", "Thêm sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý dữ liệu: " + e.getMessage());
        }

        request.getRequestDispatcher("admin/product-management.jsp").forward(request, response);
    }
}

