package com.example.webbongden.dao.model; // Đảm bảo package này đúng với project của bạn

public class Role {
    private int id;         // Sẽ tương ứng với cột 'id' trong bảng 'roles'
    private String roleName; // Sẽ tương ứng với cột 'role_name' trong bảng 'roles'

    // Constructor không tham số (cần thiết cho một số thư viện, ví dụ Jackson nếu dùng mapToBean)
    public Role() {
    }

    // Constructor đầy đủ tham số (được dùng trong AccountDao khi map kết quả từ database)
    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // (Tùy chọn) Phương thức toString() để dễ dàng debug
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}