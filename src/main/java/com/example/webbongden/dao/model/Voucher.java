package com.example.webbongden.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class Voucher {
    private int id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private Date startDate;
    private Date endDate;
    private BigDecimal minOrderValue;
    private int usageLimit;

    public Voucher() {
    }

    private int usedCount;
    private String status;

    public Voucher(int id, String status, int usedCount, int usageLimit, BigDecimal minOrderValue, Date endDate, Date startDate, BigDecimal discountValue, String discountType, String code) {
        this.id = id;
        this.status = status;
        this.usedCount = usedCount;
        this.usageLimit = usageLimit;
        this.minOrderValue = minOrderValue;
        this.endDate = endDate;
        this.startDate = startDate;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
