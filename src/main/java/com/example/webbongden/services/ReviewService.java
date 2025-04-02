package com.example.webbongden.services;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.ReviewDao;
import com.example.webbongden.dao.model.RatingCount;
import com.example.webbongden.dao.model.Review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewService {
    private final ReviewDao reviewDAO = new ReviewDao();
    private final AccountDao accountDao = new AccountDao();

    // Add a new review
    public boolean addReview(int productId, int accountId, String content, int rating, String reviewType) {
        Review review = new Review();
        review.setProductId(productId);
        review.setAccountId(accountId);
        review.setContent(content);
        review.setRating(rating);
        review.setReviewType(reviewType);

        return reviewDAO.addReview(review);
    }

    // Get all reviews for a product
    public List<Review> getReviewsByProductId(int productId, int page, int pageSize) {
        List<Review> reviews = reviewDAO.getReviewsByProductId(productId, page, pageSize);
        // Attach customer names to reviews
        for (Review review : reviews) {
            String customerName = accountDao.getCustomerNameByAccountId(review.getAccountId());
            review.setCusName(customerName);
        }
        return reviews;
    }

    public int getTotalReviewsCount(int productId) {
        return reviewDAO.getTotalReviewsCount(productId);
    }

    public List<RatingCount> getRatingCountsByProductId(int productId) {
        return reviewDAO.getRatingCounts(productId);
    }

    public static void main(String[] args) {
        ReviewService reviewService = new ReviewService();
        System.out.println(reviewService.getReviewsByProductId(1, 1, 5));
    }
}
