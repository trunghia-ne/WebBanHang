package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Notifications;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class NotiDao {
    private final Jdbi jdbi;

    public NotiDao() {
        this.jdbi = JDBIConnect.get();
    }

    // Insert notification
    public void insertNotification(Notifications notification) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("""
            INSERT INTO notifications (account_id, message, link, is_read, created_at)
            VALUES (:accountId, :message, :link, :isRead, CURRENT_TIMESTAMP)
        """)
                    .bind("accountId", notification.getAccountId()) // có thể là null
                    .bind("message", notification.getMessage())
                    .bind("link", notification.getLink())
                    .bind("isRead", notification.isRead())
                    .execute();
        });
    }


    // Load all notifications by account ID (mới nhất trước)
    public List<Notifications> getNotificationsForAdmin(Integer accountId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
            SELECT * FROM notifications
            WHERE account_id = :accountId OR account_id IS NULL
            ORDER BY created_at DESC
        """)
                        .bind("accountId", accountId)
                        .map((rs, ctx) -> new Notifications(
                                rs.getInt("id"),
                                rs.getInt("account_id"), // hoặc dùng getObject("account_id", Integer.class)
                                rs.getString("message"),
                                rs.getString("link"),
                                rs.getBoolean("is_read"),
                                rs.getTimestamp("created_at")
                        ))
                        .list()
        );
    }

    public static void main(String[] args) {
        NotiDao dao = new NotiDao();

        // Giả sử admin có accountId = 2
        Integer accountId = null;

        List<Notifications> notifications = dao.getNotificationsForAdmin(accountId);

        if (notifications.isEmpty()) {
            System.out.println("⚠️ Không có thông báo nào cho admin có accountId = " + accountId);
        } else {
            System.out.println("📥 Danh sách thông báo:");
            for (Notifications n : notifications) {
                System.out.printf("- [#%d] %s\n  → Link: %s\n  ⏱ Thời gian: %s\n\n",
                        n.getId(),
                        n.getMessage(),
                        n.getLink(),
                        n.getCreatedAt()
                );
            }
        }
    }
}
