package no.ntnu.idata2306.group6.repository;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Handle SQL database access, for products.
 */
@RepositoryRestResource
public interface ProductRepository extends CrudRepository<Product, Integer> {

//    @Query(value = "SELECT p FROM Product p join p.info")
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(int id);

    @Query(value = "SELECT i FROM Info i join i.product p where p.productId = ?1\n")
    List<Info> findInfosById(int id);
    @Query(value = "SELECT c FROM Category c JOIN c.products p WHERE p.productId = ?1\n")
    List<Category> findAllCategoriesById(int id);
    @Query(value = "SELECT p FROM Product p JOIN p.categories c WHERE c.categoryName = ?1\n")
    List<Product> findByCategoriesCategoryName(String categoryName);


}
