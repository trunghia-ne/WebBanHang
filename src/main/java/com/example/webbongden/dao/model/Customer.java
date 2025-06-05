package com.example.webbongden.dao.model;

import java.util.Date;

public class Customer {
    private int id;
    private String cusName;
    private String idNumber;
    private String sex;
    private Date birthday;
    private String address;
    private String phone;
    private String note;
    private double shippingFee;

    public Customer(int id, String cusName, String address, String phone, String note) {
        this.id = id;
        this.cusName = cusName;
        this.address = address;
        this.phone = phone;
        this.note = note;
    }

    public Customer() {

    }

    public Customer(int id, String cusName, String idNumber, String sex, Date birthday, String address, String phone, double shippingFee) { // Thêm shippingFee
        this.id = id;
        this.cusName = cusName;
        this.idNumber = idNumber;
        this.sex = sex;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
        this.shippingFee = shippingFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getShippingFee() { // <-- Thêm getter
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) { // <-- Thêm setter
        this.shippingFee = shippingFee;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", cusName='" + cusName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                ", shippingFee=" + shippingFee +
                '}';
    }
}