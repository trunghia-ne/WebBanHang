package com.example.webbongden.dao.model;

import java.util.List;

public class OrderDetailResponse {
    private List<OrderDetail> orderDetails;
    private double shippingFee;

    public OrderDetailResponse(List<OrderDetail> orderDetails, double shippingFee) {
        this.orderDetails = orderDetails;
        this.shippingFee = shippingFee;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public double getShippingFee() {
        return shippingFee;
    }
}
