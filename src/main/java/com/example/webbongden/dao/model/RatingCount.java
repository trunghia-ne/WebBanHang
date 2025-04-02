package com.example.webbongden.dao.model;

public class RatingCount {
    private int rating;
    private int count;

    public RatingCount(int rating, int count) {
        this.rating = rating;
        this.count = count;
    }

    public int getRating() {
        return rating;
    }

    public int getCount() {
        return count;
    }
}
