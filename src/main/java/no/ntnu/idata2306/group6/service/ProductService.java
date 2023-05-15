package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.repository.ProductRepository;
import no.ntnu.idata2306.group6.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

/**
 * Class for deploy services for products.
 */
@Component
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @ModelAttribute("products")
    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    /**
     * Find product by id.
     *
     * @param id of the product that shall be returned
     * @return product if found else null
     */
    @ModelAttribute("product")
    public Product findById(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    /**
     * Add product to the collection.
     *
     * @param product that shall be added
     * @return true if the product was added, and null false in case of failure
     */
    public boolean addProduct(Product product) {
        boolean added;
        if (!product.isValid() || productRepository.findById(product.getProductId()).orElse(null) == product) {
            //throw new IllegalArgumentException("Product cannot be added!");
            added = false;
        } else {
            productRepository.save(product);
            added = true;
        }
        return added;
    }

    /**
     * Remove a product from the collection.
     *
     * @param product that shall be removed from the collection
     * @return true on success and false on error
     */
    public boolean remove(Product product) {
        boolean removed;

        try {
            productRepository.delete(product);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update a product in the collection.
     *
     * @param id of the product that shall be edited
     * @param product the new product that shall be saved
     * @return null on success else errormessage
     */
    public String update(Integer id, Product product) {
        String errorMessage = null;

        Product existingProduct = findById(id);

        if (existingProduct == null) {
            errorMessage = "No existing product with id: " + id;
        } else if (product == null || !product.isValid()) {
            errorMessage = "New product is not valid";
        } else if (product.getProductId() != id) {
            errorMessage = "Id in URL does not match id in the product";
        }

        if (errorMessage == null) {
            productRepository.save(product);
        }

        return errorMessage;
    }


    public Iterable<Product> getAllByCategory(String category) {
        return productRepository.findByCategoriesCategoryName(category);
    }

    @ModelAttribute("categories")
    public Iterable<Category> getAllCategoriesByProduct(int id) {
        return productRepository.findAllCategoriesById(id);
    }

    public Iterable<Info> getInfo(int id) {
        return this.productRepository.findInfosById(id);
    }

    public Iterable<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoriesCategoryName(category);
    }
}
