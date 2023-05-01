package no.ntnu.idata2306.group6.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private SubscriptionService subscriptionService;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class.getSimpleName());

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @Operation(
            summary = "Get all subscriptions",
            description = "List of all the subscriptions currently in the collection"
    )
    public ResponseEntity<Object> getALl() {
        logger.info("Getting all ");
        Iterable<Subscription> subscriptions = subscriptionService.getAll();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PostMapping
    @Operation(deprecated = true)
    public ResponseEntity<String> addSub(@RequestBody Subscription subscription) {
        logger.info("Adding");

        ResponseEntity<String> response;

        try {
            LocalDate startDate = subscription.getStartDate();
//            logger.warn("Start date : " + startDate);
            LocalDate endDate = subscription.getEndDate();
//            logger.warn("End date : " + endDate);
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start date and end date cannot be null.");
            }
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            if (daysBetween < 0) {
                throw new IllegalArgumentException("Start date must be before end date.");
            }
            addSubscription(subscription);
            response = new ResponseEntity<>("" + subscription.getSubscriptionId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    private void addSubscription(Subscription subscription) {
        if (!subscription.isActive()) {
            throw new IllegalArgumentException("Subscription is not active");
        }
        subscriptionService.addSubscription(subscription);
    }
}