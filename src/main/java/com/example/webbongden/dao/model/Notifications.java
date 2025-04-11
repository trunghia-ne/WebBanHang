// Notifications.java
package com.example.webbongden.dao.model;

import java.sql.Timestamp;

public class Notifications {
    private int id;
    private Integer accountId;
    private String message;
    private String link;
    private boolean isRead;
    private Timestamp createdAt;

    public Notifications() {}

    public Notifications(Integer accountId, String message, String link) {
        this.accountId = accountId;
        this.message = message;
        this.link = link;
        this.isRead = false;
    }

    public Notifications(int id, Integer accountId, String message, String link, boolean isRead, Timestamp createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.message = message;
        this.link = link;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
