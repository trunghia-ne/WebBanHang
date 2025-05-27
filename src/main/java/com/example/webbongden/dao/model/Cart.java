package com.example.webbongden.dao.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<CartItem>();
    }

    public void addItem(CartItem item) {
        for (CartItem existingItem : cartItems) {
            if (existingItem.getProductId() == item.getProductId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }


    public void removeItem(int productId) {
        cartItems.removeIf(item -> item.getProductId() == productId);
    }

    public void updateQuantity(int productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    // Lấy tổng tiền tu gio hàng
    public String getTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getAmount();
        }
        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng kiểu số tiền
        return formatter.format(totalPrice);
    }

    public double getTotalPriceNumber() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getAmount();
        }
        return totalPrice;
    }

    public List<CartItem> getItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    public int getTotalQuantity() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (CartItem item : cartItems) {
            totalWeight += item.getWeight() * item.getQuantity(); // Giả sử có item.getWeight()
        }
        // Nếu không có weight chi tiết, bạn có thể đặt một giá trị mặc định
        // ví dụ: return 500; // 500 gram
        return totalWeight > 0 ? totalWeight : 500; // Trả về 500g nếu không có thông tin
    }

    // Tương tự có thể thêm getTotalLength, getTotalWidth, getTotalHeight
    // Hoặc API GHN có thể chỉ cần tổng trọng lượng và một kích thước gói hàng ước tính
    public int getEstimatedLength() { return 20; } // cm, ví dụ
    public int getEstimatedWidth() { return 15; }  // cm, ví dụ
    public int getEstimatedHeight() { return 10; } //
}
