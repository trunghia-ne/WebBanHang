package com.example.webbongden.dao.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;                  // ID của đơn hàng
    private String customerName;     // Tên khách hàng
    private Date createdAt;          // Ngày đặt hàng
    private double totalPrice;   // Tổng tiền của đơn hàng
    private String address;          // Địa chỉ giao hàng
    private String orderStatus;      // Trạng thái đơn hàng
    private List<OrderDetail> orderDetails; // Danh sách chi tiết đơn hàng (nếu cần)
    private double shippingFee;
    private String shippingMethod;
    private int accountId;
    private String note;

    // Constructors
    public Order() {}

    public Order(int id, String customerName, java.util.Date createdAt, String orderStatus) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }

    public Order(int id, String customerName, java.util.Date createdAt, double totalPrice,
                 String address, String orderStatus, List<OrderDetail> orderDetails) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }


    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getNote() {
        return note;
    }


    public void setNote(String shippingMethod) {
        this.note = shippingMethod;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getFormattedCreateAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(this.createdAt);
    }

}

