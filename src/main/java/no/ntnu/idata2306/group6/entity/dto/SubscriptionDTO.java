package no.ntnu.idata2306.group6.entity.dto;

import no.ntnu.idata2306.group6.entity.Subscription;

import java.time.LocalDate;

public class SubscriptionDTO {
    private LocalDate endDate;
    private LocalDate startDate;
    private int productId;
    private int userId;

    public LocalDate getEndDate() {
        return endDate;
    }

    public SubscriptionDTO(Subscription subscription) {
        this.endDate = subscription.getEndDate();
        this.startDate = subscription.getStartDate();
        this.productId = subscription.getProductID().getProductId();
        this.userId = subscription.getUser().getUserId();
    }

    public SubscriptionDTO(){}

    public SubscriptionDTO setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public SubscriptionDTO setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public int getProductId() {
        return productId;
    }

    public SubscriptionDTO setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public SubscriptionDTO setUserId(int userId) {
        this.userId = userId;
        return this;
    }
}
