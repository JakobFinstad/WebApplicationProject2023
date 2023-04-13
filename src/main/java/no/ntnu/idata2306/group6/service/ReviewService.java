package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Review;
import no.ntnu.idata2306.group6.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for service logic to review.
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Iterable<Review> getAll() {
        return reviewRepository.findAll();
    }

    /**
     * Find review by id.
     *
     * @param id of the review that shall be returned
     * @return review if found, else null
     */
    public Review findById(int id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.orElse(null);
    }

    /**
     * Add review to the collection.
     *
     * @param review that shall be added
     * @return true if the review was added and false in cas of failure
     */
    public boolean addReview(Review review) {
        boolean added;
        if (review.getReviewId() < 0 || reviewRepository.findById(review.getReviewId()).orElse(null) == review) {
            added = false;
        } else {
            reviewRepository.save(review);
            added = true;
        }
        return added;
    }

    /**
     * Remove a review from the collection.
     *
     * @param review that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Review review) {
        boolean removed;

        try {
            reviewRepository.delete(review);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update a review in the collection.
     *
     * @param id of the review that shall be edited
     * @param review the new review that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Review review) {
        String errorMessage = null;

        Review existingReview = findById(id);

        if (existingReview == null) {
            errorMessage = "No existing review with id: " + id;
        } else if (review == null) {
            errorMessage = "New review is invalid";
        } else if (review.getReviewId() != id) {
            errorMessage = "Id in URL does not match id in the review";
        }

        if (errorMessage == null) {
            reviewRepository.save(review);
        }

        return errorMessage;
    }

}
