package no.ntnu.idata2306.group6.Repository;

import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAll(Pageable pageable);
    Optional<Product> findByProductById(int id);
}
