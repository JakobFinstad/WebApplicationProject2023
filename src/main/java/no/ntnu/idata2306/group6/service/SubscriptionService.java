package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for service logic to subscription.
 */
@Component
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Iterable<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    /**
     * Find subscription by id.
     *
     * @param id of the subscription that shall be returned
     * @return subscription if found, else null
     */
    public Subscription findById(int id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        return subscription.orElse(null);
    }

    /**
     * Add subscription to the collection.
     *
     * @param subscription that shall be added
     * @return true if the subscription was added and false in cas of failure
     */
    public boolean addSubscription(Subscription subscription) {
        boolean added;
        if (subscription.getSubscriptionId() < 0 || subscriptionRepository.findById(subscription.getSubscriptionId()).orElse(null) == subscription) {
            added = false;
        } else {
            subscriptionRepository.save(subscription);
            added = true;
        }
        return added;
    }

    /**
     * Remove an subscription from the collection.
     *
     * @param subscription that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Subscription subscription) {
        boolean removed;

        try {
            subscriptionRepository.delete(subscription);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update an subscription in the collection.
     *
     * @param id of the subscription that shall be edited
     * @param subscription the new subscription that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Subscription subscription) {
        String errorMessage = null;

        Subscription existingSubscription = findById(id);

        if (existingSubscription == null) {
            errorMessage = "No existing subscription with id: " + id;
        } else if (subscription == null) {
            errorMessage = "New subscription is invalid";
        } else if (subscription.getSubscriptionId() != id) {
            errorMessage = "Id in URL does not match id in the subscription";
        }

        if (errorMessage == null) {
            subscriptionRepository.save(subscription);
        }

        return errorMessage;
    }

}
