package no.ntnu.idata2306.group6.Repository;

import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
