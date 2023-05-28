package no.ntnu.idata2306.group6.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * A class representing testimonials.
 *
 * @author group 6
 * @version 0.1
 */
@Entity
public class Testimonial {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @NotNull
    private String statement;

    /**
     * Constructor for a testimonial.
     *
     * @param statement statement for a testimonial.
     * @param user who wrote the testimonial
     */
    public Testimonial(String statement, User user) {
        setStatement(statement);
        this.user = user;
    }

    public Testimonial() {

    }

    /**
     * Set the user behind the testimonial.
     *
     * @param user who wrote the testimonial
     */
    private void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the user who wrote the testimonial.
     *
     * @return user who wrote the testimonial
     */
    private User getUser() {
        return this.user;
    }

    /**
     * Set the statement the user gives
     *
     * @param statement the user wrote
     */
    private void setStatement(String statement) {
        if (statement.isEmpty() || statement == null) {
            throw new IllegalArgumentException("Statement cannot be empty!");
        }
        this.statement = statement;
    }

    /**
     * Returns the statement the user wrote.
     *
     * @return the statement the user wrote
     */
    public String getStatement() {
        return this.statement;
    }

    /**
     * Set the unique review id.
     *
     * @param id of the testimonial
     */
    private void setId(int id) {
        this.reviewId = id;
    }

    /**
     * Returns the review id.
     *
     * @return the review id
     */
    public int getReviewId() {
        return reviewId;
    }
}
