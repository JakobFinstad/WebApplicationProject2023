package no.ntnu.idata2306.group6.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subID;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    private Date startDate;
    private Date endDate;

    /**
     * Class which represents a subscription.
     *
     * @param subID Subscription ID
     * @param user User ID
     * @param product Product ID
     * @param startDate When one starts the subscription
     * @param endDate When one ends the subscription
     */
    public Subscription(int subID, User user, Product product, Date startDate, Date endDate) {
        this.subID = subID;
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
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Set the subscription end date.
     *
     * @param endDate The date one ends the subscription
     */
    public void setEndDate(Date endDate) {
        if (endDate.compareTo(startDate) > 0) {
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
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Return the end date of the subscription.
     *
     * @return end date of subscription
     */
    public Date getEndDate() {
        return endDate;
    }

    public boolean isActive(){
        boolean active = false;
        Date now = Date.from(Instant.now());
        if (now.after(getStartDate()) && now.before(getEndDate())){
            active = true;
        }
        return active;
    }
}
