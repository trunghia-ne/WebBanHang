package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.User;
import org.jdbi.v3.core.Jdbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {
    private final Jdbi jdbi;

    public UserDao() {
        this.jdbi = JDBIConnect.get();
    }

    public List<User> getCustomerAccountInfo() {
        String sql = "SELECT " +
                "c.id AS customerId, " +
                "c.cus_name AS customerName, " +
                "a.email, " +
                "c.phone, " +
                "a.avatar, " +
                "c.address, " +
                "a.created_at AS registrationDate " +
                "FROM customers c " +
                "JOIN accounts a ON c.id = a.customer_id";

        List<User> users = jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new User(
                                rs.getString("customerId"),
                                rs.getString("customerName"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("avatar"),
                                rs.getString("address"),
                                rs.getDate("registrationDate"),
                                null // Tạm thời chưa gán lịch sử mua hàng
                        ))
                        .list()
        );

        // Lấy lịch sử mua hàng cho từng khách hàng
        for (User user : users) {
            List<Order> purchaseHistory = getPurchaseHistoryByCustomerId(user.getCustomerId());
            user.setOrderHistory(purchaseHistory);
        }

        return users;
    }


    public List<User> searchCustomerByName(String customerName) {
        String sql = "SELECT " +
                "c.id AS customerId, " +
                "c.cus_name AS customerName, " +
                "a.email, " +
                "a.avatar, " +
                "c.phone, " +
                "c.address, " +
                "a.created_at AS registrationDate " +
                "FROM customers c " +
                "JOIN accounts a ON c.id = a.customer_id " +
                "WHERE c.cus_name LIKE :customerName";

        List<User> users = jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("customerName", "%" + customerName + "%") // Tìm kiếm theo tên (LIKE)
                        .map((rs, ctx) -> new User(
                                rs.getString("customerId"),
                                rs.getString("customerName"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("avatar"),
                                rs.getString("address"),
                                rs.getDate("registrationDate"),
                                null // Tạm thời chưa gán lịch sử mua hàng
                        ))
                        .list()
        );

        // Lấy lịch sử mua hàng cho từng khách hàng
        for (User user : users) {
            List<Order> purchaseHistory = getPurchaseHistoryByCustomerId(user.getCustomerId());
            user.setOrderHistory(purchaseHistory);
        }

        return users;
    }

    public List<Order> getPurchaseHistoryByCustomerId(String customerId) {
        String sql = "SELECT " +
                "o.id AS orderId, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "o.order_status AS orderStatus " +
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "WHERE c.id = :customerId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("customerId", customerId)
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                null,
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"),
                                null,
                                rs.getString("orderStatus"),

                                null
                        ))
                        .list()
        );
    }

    public List<Order> getPurchaseHistoryByCustomerId2(int customerId) {
        String sql = "SELECT " +
                "o.id AS orderId, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "o.order_status AS orderStatus " +
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "WHERE c.id = :customerId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("customerId", customerId)
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                null,
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"),
                                null,
                                rs.getString("orderStatus"),
                                null
                        ))
                        .list()
        );
    }

    public int addCustomer(String cusName) {
        String sql = "INSERT INTO customers (cus_name) VALUES (:cusName)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cusName", cusName) // Gán giá trị cus_name
                        .executeAndReturnGeneratedKeys("id") // Lấy ID tự động sinh
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1) // Trả về -1 nếu thêm thất bại
        );
    }

    public int getTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM customers";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class) // Map kết quả COUNT(*) về kiểu int
                        .one() // Lấy duy nhất một giá trị
        );
    }

    public Map<String, Integer> getCustomerTypes() {
        String sql = "SELECT " +
                "    CASE " +
                "        WHEN order_count = 1 THEN 'Khách hàng mới' " +
                "        WHEN order_count BETWEEN 2 AND 5 THEN 'Khách hàng thường xuyên' " +
                "        ELSE 'Khách hàng khác' " +
                "    END AS customer_type, " +
                "    COUNT(*) AS total_customers " +
                "FROM ( " +
                "    SELECT account_id, COUNT(*) AS order_count " +
                "    FROM orders " +
                "    GROUP BY account_id " +
                ") AS order_summary " +
                "GROUP BY customer_type";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToMap()
                        .reduce(new HashMap<>(), (map, row) -> {
                            map.put(row.get("customer_type").toString(), Integer.parseInt(row.get("total_customers").toString()));
                            return map;
                        })
        );
    }

    //=====Làm login====
    public User getBasicInfoByUsername(String username) {
        String sql = "SELECT " +
                "c.id AS customerId, " + // Thêm ID khách hàng
                "c.cus_name AS customerName, " +
                "a.email AS email, " +
                "a.avatar AS avatar, " +
                "c.phone AS phoneNumber, " +
                "c.address AS address, " +
                "a.created_at AS registrationDate " +
                "FROM customers c " +
                "JOIN accounts a ON c.id = a.customer_id " +
                "WHERE a.username = :username";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username) // Gắn giá trị username vào câu truy vấn
                        .map((rs, ctx) -> new User(
                                rs.getString("customerId"), // Lấy ID khách hàng
                                rs.getString("customerName"),
                                rs.getString("email"),
                                rs.getString("phoneNumber"),
                                rs.getString("avatar"),
                                rs.getString("address"),
                                rs.getDate("registrationDate"),
                                null // Không cần lịch sử mua hàng
                        ))
                        .findOne() // Trả về một kết quả duy nhất
                        .orElse(null)
        );
    }


    public boolean updateCustomerInfo(int customerId, String cusName, String address, String phone) {
        String sql = "UPDATE customers SET cus_name = :cusName, address = :address, phone = :phone WHERE id = :customerId";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind("cusName", cusName) // Gắn giá trị mới cho tên khách hàng
                    .bind("address", address) // Gắn giá trị mới cho địa chỉ
                    .bind("phone", phone)     // Gắn giá trị mới cho số điện thoại
                    .bind("customerId", customerId) // Gắn giá trị ID khách hàng
                    .execute() > 0; // Trả về true nếu cập nhật thành công
        });
    }

    public User getCustomerById(int customerId) {
        String sql = "SELECT cus_name AS customerName, address, phone FROM customers WHERE id = :customerId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("customerId", customerId)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        int customerId = 3; // ✅ Thay bằng ID có thực trong database
        User user = userDao.getCustomerById(customerId);

        if (user != null) {
            System.out.println("🎯 Thông tin khách hàng:");
            System.out.println("Tên: " + user.getCustomerName());
            System.out.println("SĐT: " + user.getPhone());
            System.out.println("Địa chỉ: " + user.getAddress());
        } else {
            System.out.println("❌ Không tìm thấy khách hàng với ID = " + customerId);
        }
    }




//public static void main(String[] args) {
//        // Tạo đối tượng UserDao
//        UserDao userDao = new UserDao();
//
//        // Test phương thức lấy danh sách thông tin khách hàng
//        System.out.println("Danh sách thông tin khách hàng:");
//        List<User> userList = userDao.getCustomerAccountInfo();
//        for (User user : userList) {
//            System.out.println("ID Khách hàng: " + user.getCustomerId());
//            System.out.println("Tên khách hàng: " + user.getCustomerName());
//            System.out.println("Email: " + user.getEmail());
//            System.out.println("Số điện thoại: " + user.getPhone());
//            System.out.println("Địa chỉ: " + user.getAddress());
//            System.out.println("Ngày đăng ký: " + user.getCreatedAt());
//
//            // In lịch sử mua hàng nếu có
//            System.out.println("Lịch sử mua hàng:");
//            List<Order> purchaseHistory = userDao.getPurchaseHistoryByCustomerId(user.getCustomerId());
//            if (purchaseHistory != null && !purchaseHistory.isEmpty()) {
//                for (Order order : purchaseHistory) {
//                    System.out.println("  - Mã đơn hàng: " + order.getId());
//                    System.out.println("    Ngày đặt hàng: " + order.getCreatedAt());
//                    System.out.println("    Tổng tiền: " + order.getTotalPrice());
//                    System.out.println("    Trạng thái: " + order.getOrderStatus());
//                }
//            } else {
//                System.out.println("  Không có lịch sử mua hàng.");
//            }
//            System.out.println("---------------------------------------------------");
//        }
//    }
}
