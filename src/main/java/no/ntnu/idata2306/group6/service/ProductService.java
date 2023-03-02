package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.Repository.ProductRepository;
import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class for deploy services for products.
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    public Product findByID(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
}
