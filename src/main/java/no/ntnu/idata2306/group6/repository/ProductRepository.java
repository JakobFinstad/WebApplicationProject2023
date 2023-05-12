package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Handle SQL database access, for products.
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {

    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(int id);
    @Query(value = "SELECT c FROM Category c JOIN c.products p WHERE p.productId = ?1\n")
    List<Category> findAllCategoriesById(int id);
    Iterable<Product> findByCategoriesCategoryName(String categoryName);
}
