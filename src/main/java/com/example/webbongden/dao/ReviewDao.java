package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.RatingCount;
import com.example.webbongden.dao.model.Review;
import org.jdbi.v3.core.Jdbi;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class ReviewDao {
    private final Jdbi jdbi;

    public ReviewDao() {
        this.jdbi = JDBIConnect.get();
    }

    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (product_id, account_id, content, rating, review_typpe) VALUES (:productId, :accountId, :content, :rating, :reviewType)";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("productId", review.getProductId())
                        .bind("accountId", review.getAccountId())
                        .bind("content", review.getContent())
                        .bind("rating", review.getRating())
                        .bind("reviewType", "product_review")
                        .execute() > 0
        );
    }

    // In ReviewDao.java
    public List<Review> getReviewsByProductId(int productId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM reviews WHERE product_id = :productId LIMIT :limit OFFSET :offset";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .mapToBean(Review.class)
                        .list()
        );
    }

    public int getTotalReviewsCount(int productId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE product_id = :productId";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<RatingCount> getRatingCounts(int productId) {
        String sql = "SELECT rating, COUNT(*) as count FROM reviews WHERE product_id = :productId GROUP BY rating ORDER BY rating DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .map((rs, ctx) -> new RatingCount(rs.getInt("rating"), rs.getInt("count")))
                        .list()
        );
    }


}
