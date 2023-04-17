package no.ntnu.idata2306.group6.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Review;
import no.ntnu.idata2306.group6.repository.ReviewRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewRepository reviewRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class.getSimpleName());

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Get all reviews.
     * HTTP Get to /review
     *
     * @return list of all reviews currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all reviews",
            description = "List of all reviews currently stored in collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all ");
        Iterable<Review> reviews = reviewRepository.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Get a specific review.
     *
     * @param id of the returned review
     * @return review with the given id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getOne(@PathVariable Integer id) {
        ResponseEntity<Review> response;
        Optional<Review> review = reviewRepository.findById(id);
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
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody Review review) {
        ResponseEntity<String> response;

        try {
            addReviewToCollection(review);
            response = new ResponseEntity<>("" + review.getReviewId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove review from the collection.
     *
     * @param id ID of the product to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeReviewFromCollection(int id) {
        boolean deleted = false;
        try {
            reviewRepository.deleteById(id);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the review with ID: " + id + " : " + e.getMessage());
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
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        if (removeReviewFromCollection(id)) {
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
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Review review) {
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
            throw new IllegalArgumentException("Product is invalid");
        }
        reviewRepository.save(review);
    }

    /**
     * Try to update a review with given ID. The review id must match the ID.
     *
     * @param id of the review
     * @param review the update review data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateReview(int id, Review review) throws  IllegalArgumentException {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isEmpty()) {
            throw new IllegalArgumentException("No product with id " + id + " found");
        }
        if (review == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (review.getReviewId() != id) {
            throw new IllegalArgumentException("Review ID in the URL does not match the ID " +
                    "in the ID in JSON data(request body)");
        }

        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            logger.warn("Could not update review " + review.getReviewId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update review " + review.getReviewId());
        }
    }
}
