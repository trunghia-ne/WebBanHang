package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Voucher;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class VoucherDao {
    private final Jdbi jdbi;

    public VoucherDao() {
        this.jdbi = JDBIConnect.get();
    }

    // Tìm voucher theo code (chỉ lấy voucher còn active)
    public Voucher findByCode(String code) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM vouchers WHERE code = :code AND status = 'active'")
                        .bind("code", code)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // Thêm voucher mới
    public void insert(Voucher voucher) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO vouchers (code, discount_type, discount_value, start_date, end_date, min_order_value, usage_limit, status) " +
                                "VALUES (:code, :discountType, :discountValue, :startDate, :endDate, :minOrderValue, :usageLimit, :status)")
                        .bindBean(voucher)
                        .execute()
        );
    }

    // Tăng số lượt đã dùng của voucher
    public void incrementUsedCount(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE vouchers SET used_count = used_count + 1 WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    // Lấy tất cả voucher (theo thời gian tạo mới nhất)
    public List<Voucher> getAll() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM vouchers ORDER BY created_at DESC")
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    // Xóa voucher theo id
    public void delete(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM vouchers WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
}

// Update voucher
    public void update(Voucher voucher) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE vouchers SET code = :code, discount_type = :discountType, discount_value = :discountValue, " +
                                "start_date = :startDate, end_date = :endDate, min_order_value = :minOrderValue, usage_limit = :usageLimit, status = :status " +
                                "WHERE id = :id")
                        .bindBean(voucher)
                        .execute()
        );
    }

}
