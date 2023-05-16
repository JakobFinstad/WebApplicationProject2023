package no.ntnu.idata2306.group6.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subID;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * Class which represents a subscription.
     *
     * @param user User ID
     * @param product Product ID
     * @param startDate When one starts the subscription
     * @param endDate When one ends the subscription
     */
    public Subscription(User user, Product product, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.product = product;
        this.startDate = startDate;
        setEndDate(endDate);
    }

    public Subscription() {
    }

    /**
     * Set the subscription start date.
     *
     * @param startDate The date one starts the subscription
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Set the subscription end date.
     *
     * @param endDate The date one ends the subscription
     */
    public void setEndDate(LocalDate endDate) {
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("The date you wish to end your description cannot before the start date");
        }
        this.endDate = endDate;
    }

    /**
     * Return the subscription ID.
     *
     * @return ID of the subscription
     */
    public int getSubscriptionId() {
        return subID;
    }

    /**
     * Return the user.
     *
     * @return ID of the user
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Return the product.
     *
     * @return ID of the product
     */
    public Product getProductID() {
        return this.product;
    }

    /**
     * Return the start date of the subscription.
     *
     * @return start date of subscription
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Return the end date of the subscription.
     *
     * @return end date of subscription
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive(){
        boolean active = false;
        LocalDate now = LocalDate.now();
        if (now.isAfter(getStartDate()) && now.isBefore(getEndDate())){
            active = true;
        }
        return active;
    }
}
