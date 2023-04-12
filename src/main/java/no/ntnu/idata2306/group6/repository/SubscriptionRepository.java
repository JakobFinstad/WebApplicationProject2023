package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.logic.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription createSubscription(Subscription subscription);
    Subscription updateSubscription(Subscription subscription);
    Page<Subscription> findAll (Pageable pageable);
    Optional<Subscription> findById(int id);
    void deleteSubscription(int subscriptionID);
}
