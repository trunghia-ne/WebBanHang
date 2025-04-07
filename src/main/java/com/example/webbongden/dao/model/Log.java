package com.example.webbongden.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Log implements Serializable {
    private int id;
    private int accountId;
    private String level;
    private Timestamp time;
    private String action;
    private String resource;
    private String beforeData;
    private String afterData;

    public Log() {
    }

    public Log(int id, int accountId, String level, Timestamp time, String action, String resource, String beforeData, String afterData) {
        this.id = id;
        this.accountId = accountId;
        this.level = level;
        this.time = time;
        this.action = action;
        this.resource = resource;
        this.beforeData = beforeData;
        this.afterData = afterData;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }
}
