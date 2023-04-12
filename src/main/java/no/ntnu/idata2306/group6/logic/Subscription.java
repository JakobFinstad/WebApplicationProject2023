package no.ntnu.idata2306.group6.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subID;
    @ManyToOne
    private int userID;
    @ManyToOne
    private int productID;
    private Date startDate;
    private Date endDate;

    /**
     * Class which represents a subscription.
     *
     * @param subID Subscription ID
     * @param userID User ID
     * @param productID Product ID
     * @param startDate When one starts the subscription
     * @param endDate When one ends the subscription
     */
    public Subscription(int subID, int userID, int productID, Date startDate, Date endDate) {
        this.subID = subID;
        this.userID = userID;
        this.productID = productID;
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
    public int getSubID() {
        return subID;
    }

    /**
     * Return the user ID.
     *
     * @return ID of the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Return the product ID.
     *
     * @return ID of the product
     */
    public int getProductID() {
        return productID;
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
}
