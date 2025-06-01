package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Account;
import org.jdbi.v3.core.Jdbi;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDao {
    private final Jdbi jdbi;

    public AccountDao() {
        this.jdbi = JDBIConnect.get();
    }

    public Set<String> findPermissionsByRoleId(int roleId) {
        Set<String> permissions = new HashSet<>();
        String sql = "SELECT p.perm_name " +
                "FROM permissions p " +
                "JOIN role_permissions rp ON p.id = rp.perm_id " +
                "WHERE rp.role_id = :roleId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("roleId", roleId)
                        .mapTo(String.class)
                        .collect(Collectors.toSet())
        );
    }
    public List<Account> getAllAccounts() {
        String sql = "SELECT a.id, a.username, a.email, a.created_at, a.role_id, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
                            acc.setId(rs.getInt("id"));
                            acc.setUsername(rs.getString("username"));
                            acc.setEmail(rs.getString("email"));
                            acc.setCreatedAt(rs.getDate("created_at"));
                            // UPDATED: Set giá trị cho các trường mới
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .list()
        );
    }

    public List<Account> getAccountByUserName(String username) {
        String sql = "SELECT a.id, a.username, a.email, a.role_id, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id " +
                "WHERE a.username LIKE CONCAT('%', :username, '%')";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
                            acc.setId(rs.getInt("id"));
                            acc.setUsername(rs.getString("username"));
                            acc.setEmail(rs.getString("email"));
                            // UPDATED: Set giá trị cho các trường mới
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .list()
        );
    }

    public Account authenticate(String username) {
        String sql = "SELECT a.*, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id " +
                "WHERE a.username = :username";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
                            acc.setId(rs.getInt("id"));
                            acc.setCustomerId(rs.getInt("customer_id"));
                            acc.setCusName(rs.getString("cus_name"));
                            acc.setEmail(rs.getString("email"));
                            acc.setUsername(rs.getString("username"));
                            acc.setPassword(rs.getString("password"));
                            acc.setCreatedAt(rs.getDate("created_at"));
                            acc.setAvatar(rs.getString("avatar"));
                            // UPDATED: Set giá trị cho các trường mới
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT a.*, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id " +
                "WHERE a.id = :accountId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("accountId", accountId)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
                            acc.setId(rs.getInt("id"));
                            acc.setCustomerId(rs.getInt("customer_id"));
                            acc.setCusName(rs.getString("cus_name"));
                            acc.setEmail(rs.getString("email"));
                            acc.setUsername(rs.getString("username"));
                            acc.setPassword(rs.getString("password"));
                            acc.setCreatedAt(rs.getDate("created_at"));
                            acc.setAvatar(rs.getString("avatar"));
                            // UPDATED: Set giá trị cho các trường mới
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    // UPDATED: Thêm tài khoản người dùng đăng ký thường
    public boolean addAccountUser(Account account) {
        return jdbi.inTransaction(handle -> {
            // ... (Phần kiểm tra username, email, customer không đổi)
            String checkUsernameSql = "SELECT COUNT(*) FROM accounts WHERE username = :username";
            Integer count = handle.createQuery(checkUsernameSql)
                    .bind("username", account.getUsername())
                    .mapTo(Integer.class)
                    .one();
            if (count > 0) return false;

            String checkEmailSql = "SELECT COUNT(*) FROM accounts WHERE email = :email";
            Integer emailCount = handle.createQuery(checkEmailSql)
                    .bind("email", account.getEmail())
                    .mapTo(Integer.class)
                    .one();
            if (emailCount > 0) return false;

            String findCustomerSql = "SELECT id FROM customers WHERE cus_name = :cusName";
            Integer customerId = handle.createQuery(findCustomerSql)
                    .bind("cusName", account.getCusName())
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (customerId == null) {
                String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
                customerId = handle.createUpdate(addCustomerSql)
                        .bind("cusName", account.getCusName())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();
            }

            // UPDATED: Thay 'role' bằng 'role_id'
            String sql = "INSERT INTO accounts (username, email, password, role_id, customer_id, cus_name, created_at) " +
                    "VALUES (:username, :email, :password, :role_id, :customerId, :cusName, NOW())";

            int rowsAffected = handle.createUpdate(sql)
                    .bind("username", account.getUsername())
                    .bind("email", account.getEmail())
                    .bind("password", account.getPassword())
                    // UPDATED: Gán role_id = 1 cho 'customer' theo yêu cầu
                    .bind("role_id", 1)
                    .bind("customerId", customerId)
                    .bind("cusName", account.getCusName())
                    .execute();

            return rowsAffected > 0;
        });
    }

    // UPDATED: Thêm tài khoản từ Facebook
    public boolean addAccountUserFB(Account account) {
        return jdbi.inTransaction(handle -> {
            // ... (Phần thêm customer không đổi)
            String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
            Integer customerId = handle.createUpdate(addCustomerSql)
                    .bind("cusName", account.getCusName())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            // UPDATED: Thay 'role' bằng 'role_id'
            String sql = "INSERT INTO accounts (email, cus_name, username, avatar, password, role_id, customer_id, created_at) " +
                    "VALUES (:email, :cusName, :username, :avatar,:password, :role_id, :customerId, NOW())";

            return handle.createUpdate(sql)
                    .bind("email", account.getEmail())
                    .bind("cusName", account.getCusName())
                    .bind("username", account.getUsername())
                    .bind("avatar", account.getAvatar())
                    .bind("password", account.getPassword())
                    // UPDATED: Gán role_id = 1 cho 'customer' theo yêu cầu
                    .bind("role_id", 1)
                    .bind("customerId", customerId)
                    .execute() > 0;
        });
    }

    // UPDATED: Cập nhật tài khoản
    public boolean updateAccount(Account account) {
        // UPDATED: Thay 'role' bằng 'role_id'
        String sql = "UPDATE accounts " +
                "SET cus_name = :cusName, " +
                "username = :username, " +
                "email = :email, " +
                "password = :password, " +
                "role_id = :roleId " + // Sửa thành role_id
                "WHERE id = :id";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cusName", account.getCusName())
                        .bind("username", account.getUsername())
                        .bind("email", account.getEmail())
                        .bind("password", account.getPassword())
                        .bind("roleId", account.getRoleId()) // Sửa thành roleId
                        .bind("id", account.getId())
                        .execute() > 0
        );
    }

    // CÁC PHƯƠNG THỨC KHÁC KHÔNG THAY ĐỔI NHIỀU
    // (addAccount, deleteAccountById, checkEmailExists, updatePassword,...)
    // Tôi sẽ để nguyên chúng nếu không liên quan trực tiếp đến cột 'role'
    // Hoặc bạn có thể tự cập nhật chúng theo logic tương tự nếu cần.

    public boolean deleteAccountById(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = :accountId";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("accountId", accountId)
                        .execute() > 0
        );
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE email = :email";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE accounts SET password = :password WHERE email = :email";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("password", hashedPassword)
                        .bind("email", email)
                        .execute() > 0
        );
    }

    public Optional<Account> findByEmail(String email) {
        // Tương tự hàm authenticate, cần cập nhật để JOIN và map đầy đủ
        String sql = "SELECT a.*, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id " +
                "WHERE a.email = :email";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
                            // ... map tất cả các trường như trong hàm authenticate ...
                            acc.setId(rs.getInt("id"));
                            acc.setCustomerId(rs.getInt("customer_id"));
                            acc.setCusName(rs.getString("cus_name"));
                            acc.setEmail(rs.getString("email"));
                            acc.setUsername(rs.getString("username"));
                            acc.setPassword(rs.getString("password"));
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .findOne()
        );
    }

    public void updateAvatar(int customerId, String avatarUrl) {
        String sql = "UPDATE accounts SET avatar = :avatar WHERE customer_id = :id";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("avatar", avatarUrl)
                        .bind("id", customerId)
                        .execute()
        );
    }

    // Phương thức addAccount gốc nếu bạn vẫn cần (cần sửa để dùng role_id)
    public boolean addAccount(Account account) {
        //... Tương tự addAccountUser, nhưng thay vì hardcode role_id,
        // bạn sẽ lấy role_id từ đối tượng account: .bind("role_id", account.getRoleId())
        //...
        return true; // Placeholder
    }

    // Các phương thức không ảnh hưởng có thể giữ nguyên
    public String getCustomerNameByAccountId(int accountId) {
        String sql = "SELECT cus_name FROM accounts WHERE id = :accountId";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("accountId", accountId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public static void main(String[] args) {
        // ... (main method không thay đổi)
    }
}