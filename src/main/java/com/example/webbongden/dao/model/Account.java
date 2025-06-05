package com.example.webbongden.dao.model;

import java.util.Date;

public class Account {
    private int id;
    private int customerId;
    private String email;
    private String cusName;
    private String username;
    private String password;
    private Date createdAt;
    private String rePassword;
    private String avatar;

    private String role;
    private int roleId;
    private String roleName;

    private int permissionsVersion;

    public Account() {
    }

    public Account(String email, String cusName, String avatar, String username, String password) {
        this.email = email;
        this.cusName = cusName;
        this.avatar = avatar;
        this.username = username;
        this.password = password;
    }

    public Account(
            int id,
            int customerId,
            String cusName,
            String email,
            String username,
            String password,
            Date createdAt,
            String role
    ) {
        this.id = id;
        this.customerId = customerId;
        this.cusName = cusName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }

    public Account(int id, String username, String email, java.util.Date createdAt, String role) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.username = username;
        this.role = role;
    }

    public Account(int id, String username, String email, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public Account(String email, String cusName, String username, String password, String role, int customerId) {
        this.email = email;
        this.cusName = cusName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getPermissionsVersion() {
        return permissionsVersion;
    }

    public void setPermissionsVersion(int permissionsVersion) {
        this.permissionsVersion = permissionsVersion;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", email='" + email + '\'' +
                ", cusName='" + cusName + '\'' +
                ", username='" + username + '\'' +
                // ", password='" + password + '\'' + // Thường không log password
                ", createdAt=" + createdAt +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", permissionsVersion=" + permissionsVersion + // NEW
                '}';
    }
}