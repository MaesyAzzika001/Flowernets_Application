package com.example.ujicoba;

public class Review {
    private long reviewId;
    private int productId;
    private int userId;
    private long orderId;
    private int rating;
    private String comment;
    private String imagePath;
    private String timestamp;

    public Review() {}

    public Review(long reviewId, int productId, int userId, long orderId, int rating, String comment, String imagePath, String timestamp) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.rating = rating;
        this.comment = comment;
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public long getReviewId() { return reviewId; }
    public void setReviewId(long reviewId) { this.reviewId = reviewId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}