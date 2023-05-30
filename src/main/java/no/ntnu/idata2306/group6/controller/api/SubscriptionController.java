package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription API", description = "Endpoints for managing subscriptions")
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
            description = "Retrieve a list of all subscriptions in the collection",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of subscriptions",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = SubscriptionDTO.class)
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        logger.info("Getting all subscriptions");
        Iterable<Subscription> subscriptions = subscriptionService.getAll();
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();

        Iterator<Subscription> iterator = subscriptions.iterator();
        while (iterator.hasNext()) {
            subscriptionDTOs.add(new SubscriptionDTO(iterator.next()));
        }
        return new ResponseEntity<>(subscriptionDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a subscription by ID",
            description = "Retrieve a specific subscription by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Subscription found",
                            content = @Content(
                                    schema = @Schema(implementation = SubscriptionDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Subscription not found"
                    )
            }
    )
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(
            @PathVariable int id
    ) {
        logger.info("Getting subscription with ID: " + id);
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
    @Operation(
            summary = "Place a subscription order",
            description = "Place an order for a new subscription",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Subscription order placed",
                            content = @Content(
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    public ResponseEntity<String> placeSubscriptionOrder(
            @RequestBody SubscriptionDTO subscriptionDTO
    ) {
        logger.info("Placing subscription order");

        ResponseEntity<String> response;

        try {
            LocalDate startDate = subscriptionDTO.getStartDate();
            LocalDate endDate = subscriptionDTO.getEndDate();
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
                    endDate
            );
            addSubscription(subscription);
            response = new ResponseEntity<>(String.valueOf(subscription.getSubscriptionId()), HttpStatus.CREATED);
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
    @Operation(
            summary = "Delete a subscription",
            description = "Delete a subscription by its ID",
            hidden = true,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Subscription deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Subscription not found"
                    )
            }
    )
    public ResponseEntity<String> deleteSubscription(
            @PathVariable int id
    ) {
        ResponseEntity<String> response;
        Subscription subscriptionToDelete = subscriptionService.findById(id);
        if (removeSubscriptionFromCollection(subscriptionToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    private boolean removeSubscriptionFromCollection(Subscription subscription) {
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
