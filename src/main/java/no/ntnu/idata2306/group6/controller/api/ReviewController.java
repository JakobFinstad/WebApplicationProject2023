package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import no.ntnu.idata2306.group6.entity.Review;
import no.ntnu.idata2306.group6.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/review")
public class ReviewController {
    private ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class.getSimpleName());

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Get all reviews.
     * HTTP Get to /api/review
     *
     * @return list of all reviews currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all reviews",
            description = "Returns a list of all reviews currently stored in the collection"
    )
    public ResponseEntity<Iterable<Review>> getAll() {
        logger.error("Getting all reviews");
        Iterable<Review> reviews = reviewService.getAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Get a specific review.
     *
     * @param id of the returned review
     * @return review with the given id or status 404
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a specific review",
            description = "Returns the review with the given ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review found",
                            content = @Content(schema = @Schema(implementation = Review.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review not found"
                    )
            }
    )
    public ResponseEntity<Review> getOne(
            @Parameter(description = "ID of the review to retrieve") @PathVariable Integer id
    ) {
        ResponseEntity<Review> response;
        Optional<Review> review = Optional.ofNullable(reviewService.findById(id));
        if (review.isPresent()) {
            response = new ResponseEntity<>(review.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new review.
     *
     * @param review data of the review to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(
            summary = "Add a new review",
            description = "Creates a new review and returns the ID of the newly created review",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Review created",
                            content = @Content(schema = @Schema(implementation = Integer.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    public ResponseEntity<String> add(
            @Parameter(description = "Review data to add", required = true) @RequestBody Review review
    ) {
        ResponseEntity<String> response;

        try {
            addReviewToCollection(review);
            response = new ResponseEntity<>(String.valueOf(review.getReviewId()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove review from the collection.
     *
     * @param review to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeReviewFromCollection(Review review) {
        boolean deleted = false;
        try {
            reviewService.remove(review);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the review with ID: " + review.getReviewId() + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete a review from the collection.
     *
     * @param id ID of the review to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a review",
            description = "Deletes the review with the given ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review not found"
                    )
            }
    )
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the review to delete") @PathVariable int id
    ) {
        ResponseEntity<String> response;
        Review reviewToDelete = reviewService.findById(id);
        if (removeReviewFromCollection(reviewToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update a review in the repository.
     *
     * @param id of the review to update, from the URL
     * @param review new review data to store, from request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a review",
            description = "Updates the review with the given ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
    public ResponseEntity<String> update(
            @Parameter(description = "ID of the review to update") @PathVariable int id,
            @Parameter(description = "Review data to update", required = true) @RequestBody Review review
    ) {
        ResponseEntity<String> response;
        try {
            updateReview(id, review);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Add a review to collection.
     *
     * @param review the review to be added to collection if it is valid
     * @throws IllegalArgumentException
     */
    private void addReviewToCollection(Review review) throws IllegalArgumentException {
        if (review == null || review.getReviewId() < 0) {
            throw new IllegalArgumentException("Review is invalid");
        }
        reviewService.addReview(review);
    }

    /**
     * Try to update a review with given ID. The review id must match the ID.
     *
     * @param id of the review
     * @param review the update review data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateReview(int id, Review review) throws  IllegalArgumentException {
        Optional<Review> existingReview = Optional.ofNullable(reviewService.findById(id));
        if (existingReview.isEmpty()) {
            throw new IllegalArgumentException("No review with ID " + id + " found");
        }
        if (review == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (review.getReviewId() != id) {
            throw new IllegalArgumentException("Review ID in the URL does not match the ID " +
                    "in the JSON data (request body)");
        }

        try {
            reviewService.update(id, review);
        } catch (Exception e) {
            logger.warn("Could not update review " + review.getReviewId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update review " + review.getReviewId());
        }
    }
}
