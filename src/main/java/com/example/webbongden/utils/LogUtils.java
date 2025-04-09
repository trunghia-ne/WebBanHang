package com.example.webbongden.utils;

import com.example.webbongden.dao.model.Cart;
import com.example.webbongden.dao.model.CartItem;
import com.example.webbongden.dao.model.Invoices;
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
}
