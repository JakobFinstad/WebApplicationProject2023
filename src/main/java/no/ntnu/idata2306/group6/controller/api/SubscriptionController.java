package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.entity.dto.SubscriptionDTO;
import no.ntnu.idata2306.group6.service.ProductService;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import no.ntnu.idata2306.group6.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;
    private UserService userService;
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class.getSimpleName());

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService, ProductService productService) {
        this.subscriptionService = subscriptionService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all subscriptions",
            description = "List of all the subscriptions currently in the collection"
    )
    public ResponseEntity<Object> getALl() {
        logger.info("Getting all ");
        Iterable<Subscription> subscriptions = subscriptionService.getAll();
        List<SubscriptionDTO> subscriptionDTOS = new ArrayList<>();

        Iterator<Subscription> it = subscriptions.iterator();
        while (it.hasNext()) {
            subscriptionDTOS.add(new SubscriptionDTO(it.next()));
        }
        return new ResponseEntity<>(subscriptionDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get one subscription",
            description = "Get out one subscription in the collection"
    )
    public ResponseEntity<SubscriptionDTO> getOne(@PathVariable int id) {
        logger.info("Getting sub with id: " + id);
        ResponseEntity<SubscriptionDTO> response;
        Optional<Subscription> subscription = Optional.ofNullable(subscriptionService.findById(id));
        if (subscription.isPresent()) {
            response = new ResponseEntity<>(new SubscriptionDTO(subscription.get()), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("place-order")
    @Operation(deprecated = true)
    public ResponseEntity<String> addSub(@RequestBody SubscriptionDTO subscriptionDTO) {
        logger.info("Adding");

        ResponseEntity<String> response;

        try {
            LocalDate startDate = subscriptionDTO.getStartDate();
//            logger.warn("Start date : " + startDate);
            LocalDate endDate = subscriptionDTO.getEndDate();
//            logger.warn("End date : " + endDate);
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start date and end date cannot be null.");
            }
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            if (daysBetween < 0) {
                throw new IllegalArgumentException("Start date must be before end date.");
            }
            Subscription subscription = new Subscription(
                    userService.findById(subscriptionDTO.getUserId()).get(),
                    productService.findById(subscriptionDTO.getProductId()),
                    startDate,
                    endDate);
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

    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        Subscription subscriptionToDelete = subscriptionService.findById(id);
        if (removeSubFromCollection(subscriptionToDelete)) {
            response = new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    private boolean removeSubFromCollection(Subscription subscription) {
        boolean deleted = false;
        try {
            subscriptionService.remove(subscription);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete subscription");
        }
        return deleted;
    }
}