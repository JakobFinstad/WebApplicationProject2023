package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TestimonialRepository  extends CrudRepository<Testimonial, Integer> {
    Page<Testimonial> findAll(Pageable pageable);

    Optional<Testimonial> findById(int id);

    @Query(value = "SELECT t FROM Testimonial t JOIN User u WHERE u.userId = ?1\n")
    Iterable<Testimonial> findByUserId(int userId);
}
