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

    public String getTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getAmount();
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
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
            totalWeight += item.getWeight() * item.getQuantity();
        }
        return totalWeight > 0 ? totalWeight : 500;
    }

    public int getEstimatedLength() { return 20; }
    public int getEstimatedWidth() { return 15; }
    public int getEstimatedHeight() { return 10; }
}
