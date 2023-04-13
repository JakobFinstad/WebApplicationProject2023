package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(int id);
}
