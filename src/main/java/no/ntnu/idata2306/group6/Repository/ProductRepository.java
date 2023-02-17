package no.ntnu.idata2306.group6.Repository;

import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
