package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAll(Sort sort);

    Page<Product> findAll(Pageable pageable);
}
