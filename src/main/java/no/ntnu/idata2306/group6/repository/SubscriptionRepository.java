package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Subscription;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SubscriptionRepository extends Repository<Subscription, Integer> {
    Subscription createSubscription(Subscription subscription);
    Subscription updateSubscription(Subscription subscription);
    Page<Subscription> findAll (Pageable pageable);
    Optional<Subscription> findById(int id);
    void deleteSubscription(int subscriptionID);
}
