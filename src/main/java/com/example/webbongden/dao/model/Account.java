package com.example.webbongden.dao.model;

import java.util.Date;

public class Account {
    private int id; // ID của tài khoản
    private int customerId; // ID của khách hàng liên kết
    private String email; // Email tài khoản
    private String cusName; // Tên khách hàng
    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
    private Date createdAt; // Ngày tạo tài khoản
    private String rePassword;
    private String avatar;

    // Các trường liên quan đến vai trò và phân quyền mới
    private String role;     // Giữ lại để tương thích ngược (sẽ được Jackson map từ JSON nếu client gửi "role")
    private int roleId;      // ID của vai trò (lấy từ DB)
    private String roleName;  // Tên của vai trò (lấy từ DB)

    // NEW: Thêm trường permissionsVersion
    private int permissionsVersion;

    // Constructor không tham số
    public Account() {
    }

    // Các constructor hiện tại của bạn, giữ nguyên để không làm lỗi code cũ
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
            String role // Vẫn nhận role String từ các nguồn cũ nếu có
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

    // --- GETTER & SETTER CHO CÁC TRƯỜNG MỚI VÀ CẬP NHẬT ---
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

    // NEW: Getter và Setter cho permissionsVersion
    public int getPermissionsVersion() {
        return permissionsVersion;
    }

    public void setPermissionsVersion(int permissionsVersion) {
        this.permissionsVersion = permissionsVersion;
    }
    // -----------------------------------------------------

    // Các Getter & Setter cũ vẫn được giữ nguyên
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    // Getter/Setter cho trường 'role' (String) vẫn giữ lại để Jackson map JSON
    // và để tương thích nếu có code cũ đang dùng
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Cập nhật toString() để bao gồm cả permissionsVersion
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