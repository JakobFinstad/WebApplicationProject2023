package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Page<Review> findAll(Pageable pageable);
    Optional<Review> findById(int id);

    @Query(value = "SELECT r FROM Review r JOIN Product p WHERE p.productId = ?1\n")
    Optional<Review> findByProdId(int id);
}
