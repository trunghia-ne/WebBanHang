
package com.example.webbongden.dao.model;

public class OrderDetail {
    private int productId;        // ID của sản phẩm
    private int orderId;          // ID của đơn hàng
    private int quantity;         // Số lượng sản phẩm
    private double unitPrice;     // Giá mỗi sản phẩm
    private double itemDiscount;  // Chiết khấu trên từng sản phẩm
    private double amount;        // Tổng tiền (sau chiết khấu)
    private String productName;
    private String productImageUrl;
    private String paymentMethod;
    private String paymentStatus;
    private String phoneNumber;

    // Constructors
    public OrderDetail() {}

    public OrderDetail(int productId, int orderId, int quantity, double unitPrice,
                       double itemDiscount, double amount) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemDiscount = itemDiscount;
        this.amount = amount;
    }

    public OrderDetail(int productId, int quantity, double unitPrice, double itemDiscount, double amount) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemDiscount = itemDiscount;
        this.amount = amount;
    }

    public OrderDetail(int productId, int orderId, String productName, int quantity, double unitPrice, double itemDiscount, double amount, String productImageUrl, String paymentMethod, String paymentStatus, String phoneNumber) {
        this.productId = productId;
        this.orderId = orderId;
        this.productName = productName; // Gán giá trị tên sản phẩm
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemDiscount = itemDiscount;
        this.amount = amount;
        this.productImageUrl = productImageUrl;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.phoneNumber = phoneNumber;
    }




    // Getters and Setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {}
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}


