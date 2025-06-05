package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Role;
import org.jdbi.v3.core.Jdbi;

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

    public List<Role> getAllRoles() {
        String sql = "SELECT id, role_name FROM roles ORDER BY role_name ASC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Role(rs.getInt("id"), rs.getString("role_name")))
                        .list()
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
                            acc.setRoleId(rs.getInt("role_id"));
                            acc.setRoleName(rs.getString("role_name"));
                            return acc;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean addAccountUser(Account account) {
        return jdbi.inTransaction(handle -> {
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

            String sql = "INSERT INTO accounts (username, email, password, role_id, customer_id, cus_name, created_at) " +
                    "VALUES (:username, :email, :password, :role_id, :customerId, :cusName, NOW())";

            int rowsAffected = handle.createUpdate(sql)
                    .bind("username", account.getUsername())
                    .bind("email", account.getEmail())
                    .bind("password", account.getPassword())
                    .bind("role_id", 1)
                    .bind("customerId", customerId)
                    .bind("cusName", account.getCusName())
                    .execute();

            return rowsAffected > 0;
        });
    }

    public boolean addAccountUserFB(Account account) {
        return jdbi.inTransaction(handle -> {
            String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
            Integer customerId = handle.createUpdate(addCustomerSql)
                    .bind("cusName", account.getCusName())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            String sql = "INSERT INTO accounts (email, cus_name, username, avatar, password, role_id, customer_id, created_at) " +
                    "VALUES (:email, :cusName, :username, :avatar,:password, :role_id, :customerId, NOW())";

            return handle.createUpdate(sql)
                    .bind("email", account.getEmail())
                    .bind("cusName", account.getCusName())
                    .bind("username", account.getUsername())
                    .bind("avatar", account.getAvatar())
                    .bind("password", account.getPassword())
                    .bind("role_id", 1)
                    .bind("customerId", customerId)
                    .execute() > 0;
        });
    }

    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts " +
                "SET cus_name = :cusName, " +
                "username = :username, " +
                "email = :email, " +
                "password = :password, " +
                "role_id = :roleId " +
                "WHERE id = :id";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cusName", account.getCusName())
                        .bind("username", account.getUsername())
                        .bind("email", account.getEmail())
                        .bind("password", account.getPassword())
                        .bind("roleId", account.getRoleId())
                        .bind("id", account.getId())
                        .execute() > 0
        );
    }

    public boolean addAccount(Account account) {
        return jdbi.inTransaction(handle -> {
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

            if (customerId == null && account.getCusName() != null && !account.getCusName().isEmpty()) {
                String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
                customerId = handle.createUpdate(addCustomerSql)
                        .bind("cusName", account.getCusName())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();
            }


            // Câu lệnh INSERT
            String sql = "INSERT INTO accounts (username, email, password, role_id, customer_id, cus_name, created_at) " +
                    "VALUES (:username, :email, :password, :roleId, :customerId, :cusName, NOW())";

            int rowsAffected = handle.createUpdate(sql)
                    .bind("username", account.getUsername())
                    .bind("email", account.getEmail())
                    .bind("password", account.getPassword())
                    .bind("roleId", account.getRoleId())
                    .bind("customerId", customerId)
                    .bind("cusName", account.getCusName())
                    .execute();

            return rowsAffected > 0;
        });
    }

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
        String sql = "SELECT a.*, r.role_name " +
                "FROM accounts a " +
                "JOIN roles r ON a.role_id = r.id " +
                "WHERE a.email = :email";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .map((rs, ctx) -> {
                            Account acc = new Account();
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
    }
}