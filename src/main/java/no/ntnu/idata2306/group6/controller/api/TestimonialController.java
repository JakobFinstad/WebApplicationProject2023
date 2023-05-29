package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Testimonial;
import no.ntnu.idata2306.group6.entity.dto.TestimonialDTO;
import no.ntnu.idata2306.group6.service.TestimonialService;
import no.ntnu.idata2306.group6.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testimonial")
public class TestimonialController {
    private TestimonialService testimonialService;
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TestimonialController.class.getSimpleName());

    @Autowired
    public TestimonialController(TestimonialService testimonialService, UserService userService) {
        this.testimonialService = testimonialService;
        this.userService = userService;
    }

    /**
     * Get all testimonials.
     * HTTP Get to /testimonial
     *
     * @return list of all testimonials currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all testimonials",
            description = "List of all testimonials currently stored in collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all ");
        Iterable<Testimonial> testimonials = testimonialService.getAll();
        List<TestimonialDTO> testimonialDTOS = new ArrayList<>();
        for (Testimonial te: testimonials) {
            testimonialDTOS.add(new TestimonialDTO()
                    .setTestimonialId(te.getTestimonialId())
                    .setStatement(te.getStatement())
                            .setUserName("" + te.getUser().getFirstName() + " " + te.getUser().getLastName())
                    .setUserImageSrc(te.getUser().getImgURL()));
        }
        return new ResponseEntity<>(testimonialDTOS.iterator(), HttpStatus.OK);
    }

    /**
     * Get a specific testimonial.
     *
     * @param id of the returned testimonial
     * @return testimonial with the given id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Testimonial> getOne(@PathVariable Integer id) {
        ResponseEntity<Testimonial> response;
        Optional<Testimonial> testimonial = Optional.ofNullable(testimonialService.findById(id));
        if (testimonial.isPresent()) {
            response = new ResponseEntity<>(testimonial.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new testimonial.
     *
     * @param testimonial data of the testimonial to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody Testimonial testimonial) {
        ResponseEntity<String> response;

        try {
            addTestimonialToCollection(testimonial);
            response = new ResponseEntity<>("" + testimonial.getTestimonialId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove testimonial from the collection.
     *
     * @param testimonial to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeTestimonialFromCollection(Testimonial testimonial) {
        boolean deleted = false;
        try {
            testimonialService.remove(testimonial);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the testimonial with ID: " + testimonial.getTestimonialId() + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete a testimonial from the collection.
     *
     * @param id ID of the testimonial to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        Testimonial testimonialToDelete = testimonialService.findById(id);
        if (removeTestimonialFromCollection(testimonialToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update a testimonial in the repository.
     *
     * @param id of the testimonial to update, from the URL
     * @param testimonial new testimonial data to store, from request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Testimonial testimonial) {
        ResponseEntity<String> response;
        try {
            updateTestimonial(id, testimonial);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Add a testimonial to collection.
     *
     * @param testimonial the testimonial to be added to collection if it is valid
     * @throws IllegalArgumentException
     */
    private void addTestimonialToCollection(Testimonial testimonial) throws IllegalArgumentException {
        if (testimonial == null || testimonial.getTestimonialId() < 0) {
            throw new IllegalArgumentException("Product is invalid");
        }
        testimonialService.addTestimonial(testimonial);
    }

    /**
     * Try to update a testimonial with given ID. The testimonial id must match the ID.
     *
     * @param id of the testimonial
     * @param testimonial the update testimonial data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateTestimonial(int id, Testimonial testimonial) throws  IllegalArgumentException {
        Optional<Testimonial> existingTestimonial = Optional.ofNullable(testimonialService.findById(id));
        if (existingTestimonial.isEmpty()) {
            throw new IllegalArgumentException("No product with id " + id + " found");
        }
        if (testimonial == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (testimonial.getTestimonialId() != id) {
            throw new IllegalArgumentException("Testimonial" +
                     "ID in the URL does not match the ID " +
                    "in the ID in JSON data(request body)");
        }

        try {
            testimonialService.update(id, testimonial);
        } catch (Exception e) {
            logger.warn("Could not update testimonial " + testimonial.getTestimonialId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update testimonial " + testimonial.getTestimonialId());
        }
    }
}
