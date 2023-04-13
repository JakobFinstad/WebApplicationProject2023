package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findAll(Pageable pageable);
    Optional<Review> findById(int id);
}
