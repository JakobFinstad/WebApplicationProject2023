package no.ntnu.idata2306.group6.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Review {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @NotNull
    private String statement;

    private int rating;

    /**
     * Constructor for the review class.
     *
     * @param rating 1 to 5 rating for the service
     * @param statement the statement given by the user
     */
    public Review(int rating, String statement, User user, Product product) {
        setRating(rating);
        setStatement(statement);
        this.user = user;
        setProduct(product);
    }

    public Review() {

    }

    private void setUser(User user) {
        this.user = user;
    }
    private User getUser() {
        return this.user;
    }
    private void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    /**
     * Get the rating of the review.
     *
     * @return rating of the review
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Set rating of this review.
     *
     * @param rating new rating given by the user
     */
    private void setRating(int rating) {
        if (rating < 1 || rating >5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5!");
        }
        this.rating = rating;
    }

    /**
     * Set the statement of the review.
     *
     * @param statement given by the user
     */
    private void setStatement(String statement) {
        if (statement.isEmpty() || statement == null) {
            throw new IllegalArgumentException("Statement cannot be empty!");
        }
        this.statement = statement;
    }

    /**
     * Get the statement of this review.
     *
     * @return statement of this review.
     */
    public String getStatement() {
        return this.statement;
    }

    /**
     * Set id for the review.
     *
     * @param id new id
     */
    private void setId(int id) {
        this.reviewId = id;
    }

    /**
     * Get the id of this review.
     *
     * @return id of this review
     */
    public int getReviewId() {
        return reviewId;
    }
}
