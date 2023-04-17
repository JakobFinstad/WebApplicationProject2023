package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Page<Review> findAll(Pageable pageable);
    Optional<Review> findById(int id);
}
