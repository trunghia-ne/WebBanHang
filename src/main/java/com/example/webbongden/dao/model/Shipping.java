package com.example.webbongden.dao.model;

import java.util.Date;

public class Shipping {
    private int id;
    private int orderId;
    private Date pickupDate;
    private String shippingStatus;
    private String address;
    private String carrier;
    private String phoneNumber;
    private String cusName;
    private double shippingFee;

    // Constructor không tham số
    public Shipping() {
    }

    // Constructor có tham số
    public Shipping(int id, int orderId, Date pickupDate, String shippingStatus, String address, String carrier, String phoneNumber,String cusName, double shippingFee) {
        this.id = id;
        this.orderId = orderId;
        this.pickupDate = pickupDate;
        this.shippingStatus = shippingStatus;
        this.address = address;
        this.carrier = carrier;
        this.phoneNumber = phoneNumber;
        this.cusName = cusName;
        this.shippingFee = shippingFee;
    }

    // Getter và Setter
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }
    // Phương thức toString
    @Override
    public String toString() {
        return "Shipping{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", pickupDate=" + pickupDate +
                ", shippingStatus='" + shippingStatus + '\'' +
                ", address='" + address + '\'' +
                ", carrier='" + carrier + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cusName='" + cusName + '\'' +
                ", shippingFee=" + shippingFee +
                '}';
    }
}
