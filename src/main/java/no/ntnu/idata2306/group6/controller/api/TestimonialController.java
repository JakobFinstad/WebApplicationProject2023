package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Testimonial API", description = "Endpoints for managing testimonials")
public class TestimonialController {
    private TestimonialService testimonialService;
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TestimonialController.class.getSimpleName());

    @Autowired
    public TestimonialController(TestimonialService testimonialService, UserService userService) {
        this.testimonialService = testimonialService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all testimonials",
            description = "Retrieve a list of all testimonials currently stored in the collection",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of testimonials",
                            content = @Content(
                                    schema = @Schema(implementation = TestimonialDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all testimonials");
        Iterable<Testimonial> testimonials = testimonialService.getAll();
        List<TestimonialDTO> testimonialDTOS = new ArrayList<>();
        for (Testimonial te : testimonials) {
            testimonialDTOS.add(new TestimonialDTO()
                    .setTestimonialId(te.getTestimonialId())
                    .setStatement(te.getStatement())
                    .setUserName("" + te.getUser().getFirstName() + " " + te.getUser().getLastName())
                    .setUserImageSrc(te.getUser().getImgURL()));
        }
        return new ResponseEntity<>(testimonialDTOS.iterator(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a specific testimonial",
            description = "Retrieve a specific testimonial by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Testimonial found",
                            content = @Content(
                                    schema = @Schema(implementation = Testimonial.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Testimonial not found"
                    )
            }
    )
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

    @PostMapping()
    @Operation(
            summary = "Add a new testimonial",
            description = "Add a new testimonial to the collection",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Testimonial added",
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

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a testimonial",
            description = "Delete a testimonial by its ID",
            hidden = true,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Testimonial deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Testimonial not found"
                    )
            }
    )
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

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a testimonial",
            description = "Update a testimonial in the collection",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Testimonial updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }
    )
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

    private void addTestimonialToCollection(Testimonial testimonial) throws IllegalArgumentException {
        if (testimonial == null || testimonial.getTestimonialId() < 0) {
            throw new IllegalArgumentException("Testimonial is invalid");
        }
        testimonialService.addTestimonial(testimonial);
    }

    private void updateTestimonial(int id, Testimonial testimonial) throws IllegalArgumentException {
        Optional<Testimonial> existingTestimonial = Optional.ofNullable(testimonialService.findById(id));
        if (existingTestimonial.isEmpty()) {
            throw new IllegalArgumentException("No testimonial with ID " + id + " found");
        }
        if (testimonial == null) {
            throw new IllegalArgumentException("Wrong data in the request body");
        }
        if (testimonial.getTestimonialId() != id) {
            throw new IllegalArgumentException("Testimonial ID in the URL does not match the ID in the request body");
        }

        try {
            testimonialService.update(id, testimonial);
        } catch (Exception e) {
            logger.warn("Could not update testimonial " + testimonial.getTestimonialId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update testimonial " + testimonial.getTestimonialId());
        }
    }
}
