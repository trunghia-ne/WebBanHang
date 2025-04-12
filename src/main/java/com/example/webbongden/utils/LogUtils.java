package com.example.webbongden.utils;

import com.example.webbongden.dao.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;

public class LogUtils {
    public static void logCreateOrder(HttpServletRequest request, Invoices invoice, Cart cart, String paymentMethod) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("invoiceId", invoice.getId());
            afterData.put("accountId", invoice.getAccountId());
            afterData.put("totalPrice", invoice.getTotalPrice());
            afterData.put("paymentMethod", paymentMethod);
            afterData.put("status", invoice.getPaymentStatus());
            afterData.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoice.getCreatedAt()));

            JSONArray cartArray = new JSONArray();
            for (CartItem item : cart.getItems()) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("productId", item.getProductId());
                itemJson.put("productName", item.getProductName());
                itemJson.put("quantity", item.getQuantity());
                itemJson.put("unitPrice", item.getPrice());
                itemJson.put("amount", item.getQuantity() * item.getPrice());
                cartArray.put(itemJson);
            }

            afterData.put("cartItems", cartArray);

            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace(); // hoặc log lỗi nếu cần
        }
    }

    public static void logAddProduct(HttpServletRequest request, ProductDetail product) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("productName", product.getProductName());
            afterData.put("unitPrice", product.getUnitPrice());
            afterData.put("stockQuantity", product.getStockQuantity());
            afterData.put("productStatus", product.getProductStatus());
            afterData.put("description", product.getDescription());
            afterData.put("warrantyPeriod", product.getWarrantyPeriod());
            afterData.put("lightColor", product.getLightColor());
            afterData.put("material", product.getMaterial());
            afterData.put("voltage", product.getVoltage());
            afterData.put("usageAge", product.getUsageAge());
            afterData.put("discountPercent", product.getDiscountPercent());
            afterData.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getCreatedAt()));

            JSONArray imageArray = new JSONArray();
            for (ProductImage image : product.getListImages()) {
                JSONObject imageJson = new JSONObject();
                imageJson.put("url", image.getUrl());
                imageJson.put("mainImage", image.isMainImage());
                imageArray.put(imageJson);
            }

            afterData.put("images", imageArray);
            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logAddProductImage(HttpServletRequest request, int productId, String imageUrl) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("productId", productId);
            afterData.put("url", imageUrl);
            afterData.put("mainImage", false);

            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logDeleteProduct(HttpServletRequest request, int productId) {
        try {
            JSONObject beforeData = new JSONObject();
            beforeData.put("id", productId);
            request.setAttribute("beforeData", beforeData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logDeleteProductImage(HttpServletRequest request, int imageId, int productId) {
        try {
            JSONObject beforeData = new JSONObject();
            beforeData.put("imageId", imageId);
            beforeData.put("productId", productId);

            request.setAttribute("beforeData", beforeData.toString());
            request.setAttribute("after_data", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUpdateProduct(HttpServletRequest request, ProductDetail before, ProductDetail after) {
        try {
            JSONObject beforeData = new JSONObject();
            beforeData.put("id", before.getId());
            beforeData.put("productName", before.getProductName());
            beforeData.put("unitPrice", before.getUnitPrice());
            beforeData.put("stockQuantity", before.getStockQuantity());
            beforeData.put("productStatus", before.getProductStatus());
            beforeData.put("rating", before.getRating());
            beforeData.put("description", before.getDescription());
            beforeData.put("warrantyPeriod", before.getWarrantyPeriod());
            beforeData.put("lightColor", before.getLightColor());
            beforeData.put("material", before.getMaterial());
            beforeData.put("voltage", before.getVoltage());
            beforeData.put("usageAge", before.getUsageAge());
            beforeData.put("discountPercent", before.getDiscountPercent());
            beforeData.put("subCategoryId", before.getSubCategoryId());

            JSONObject afterData = new JSONObject();
            afterData.put("id", after.getId());
            afterData.put("productName", after.getProductName());
            afterData.put("unitPrice", after.getUnitPrice());
            afterData.put("stockQuantity", after.getStockQuantity());
            afterData.put("productStatus", after.getProductStatus());
            afterData.put("rating", after.getRating());
            afterData.put("description", after.getDescription());
            afterData.put("warrantyPeriod", after.getWarrantyPeriod());
            afterData.put("lightColor", after.getLightColor());
            afterData.put("material", after.getMaterial());
            afterData.put("voltage", after.getVoltage());
            afterData.put("usageAge", after.getUsageAge());
            afterData.put("discountPercent", after.getDiscountPercent());
            afterData.put("subCategoryId", after.getSubCategoryId());

            request.setAttribute("beforeData", beforeData.toString());
            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logAddAccount(HttpServletRequest request, Account account) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("cus_name", account.getCusName());
            afterData.put("username", account.getUsername());
            afterData.put("email", account.getEmail());
            afterData.put("role", account.getRole());
            afterData.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUpdateAccount(HttpServletRequest request, Account before, Account after) {
        try {
            JSONObject beforeData = new JSONObject();
            beforeData.put("id", before.getId());
            beforeData.put("username", before.getUsername());
            beforeData.put("cus_name", before.getCusName());
            beforeData.put("email", before.getEmail());
            beforeData.put("role", before.getRole());


            JSONObject afterData = new JSONObject();
            afterData.put("id", after.getId());
            afterData.put("username", after.getUsername());
            afterData.put("cus_name", before.getCusName());
            afterData.put("email", after.getEmail());
            afterData.put("role", after.getRole());

            request.setAttribute("beforeData", beforeData.toString());
            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logAddPromotion(HttpServletRequest request, Promotion promotion) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("id", promotion.getId());
            afterData.put("promotionName", promotion.getPromotionName());
            afterData.put("startDate", promotion.getStartDay());
            afterData.put("endDate", promotion.getEndDay());
            afterData.put("percentDiscount", promotion.getDiscountPercent());
            afterData.put("type", promotion.getPromotionType());

            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logAddProductToPromotion(HttpServletRequest request, int promotionId, int productId) {
        try {
            JSONObject afterData = new JSONObject();
            afterData.put("promotionId", promotionId);
            afterData.put("productId", productId);

            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUpdateOrderStatus(HttpServletRequest request, int orderId, String oldStatus, String newStatus) {
        try {
            JSONObject beforeData = new JSONObject();
            beforeData.put("orderId", orderId);
            beforeData.put("status", oldStatus);

            JSONObject afterData = new JSONObject();
            afterData.put("orderId", orderId);
            afterData.put("status", newStatus);

            request.setAttribute("beforeData", beforeData.toString());
            request.setAttribute("afterData", afterData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
