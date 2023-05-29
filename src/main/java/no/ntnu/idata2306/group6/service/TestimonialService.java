package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Testimonial;
import no.ntnu.idata2306.group6.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service class for service logic to testimonial.
 */
@Component
public class TestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    public Iterable<Testimonial> getAll() {
        return testimonialRepository.findAll();
    }

    /**
     * Find testimonial by id.
     *
     * @param id of the testimonial that shall be returned
     * @return testimonial if found, else null
     */
    public Testimonial findById(int id) {
        Optional<Testimonial> testimonial = testimonialRepository.findById(id);
        return testimonial.orElse(null);
    }

    /**
     * Add testimonial to the collection.
     *
     * @param testimonial that shall be added
     * @return true if the testimonial was added and false in cas of failure
     */
    public boolean addTestimonial(Testimonial testimonial) {
        boolean added;
        if (testimonial.getTestimonialId() < 0 || testimonialRepository.findById(testimonial.getTestimonialId()).orElse(null) == testimonial) {
            added = false;
        } else {
            testimonialRepository.save(testimonial);
            added = true;
        }
        return added;
    }

    /**
     * Remove a testimonial from the collection.
     *
     * @param testimonial that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Testimonial testimonial) {
        boolean removed;

        try {
            testimonialRepository.delete(testimonial);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update a testimonial in the collection.
     *
     * @param id of the testimonial that shall be edited
     * @param testimonial the new testimonial that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Testimonial testimonial) {
        String errorMessage = null;

        Testimonial existingTestimonial = findById(id);

        if (existingTestimonial == null) {
            errorMessage = "No existing testimonial with id: " + id;
        } else if (testimonial == null) {
            errorMessage = "New testimonial is invalid";
        } else if (testimonial.getTestimonialId() != id) {
            errorMessage = "Id in URL does not match id in the testimonial";
        }

        if (errorMessage == null) {
            testimonialRepository.save(testimonial);
        }

        return errorMessage;
    }
}
