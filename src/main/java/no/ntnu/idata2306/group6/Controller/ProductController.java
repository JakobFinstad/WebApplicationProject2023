package no.ntnu.idata2306.group6.Controller;

import no.ntnu.idata2306.group6.Repository.ProductRepository;
import no.ntnu.idata2306.group6.logic.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.*;

/**
 * REST API controller for product collection
 */
@RestController
@RequestMapping("/product")
public class ProductController {
  private ProductRepository productRepository;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class.getSimpleName());

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Get all products
   * HTTP GET to /product
   *
   * @return List of all products currently stored in the collection; or 500 code on error.
   */
  @GetMapping
  @Operation(
      summary = "Get all products",
      description = "List of all products currently stored in collection"
  )
  public ResponseEntity<Object> getAll() {
    logger.error("Getting all ");
    Iterable<Product> products = productRepository.findAll();
    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  /**
   * Get a specific product
   *
   * @param id ID of the returned product
   * @return Product with the given ID or status 404
   */
   @GetMapping("/{id}")
  public ResponseEntity<Product> getOne(@PathVariable Integer id) {
    ResponseEntity<Product> response;
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      response = new ResponseEntity<>(product.get(), HttpStatus.OK);
    } else {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
   }

  /**
   * HTTP POST endpoint for adding a new product.
   *
   * @param product Data of the book to add. ID will be ignored.
   * @return 201 Created on success and the new ID in the response body,
   * 400 Bad request if some data is missing or incorrect
   */
  @PostMapping()
  @Operation(deprecated = true)
  ResponseEntity<String> add(@RequestBody Product product) {
    ResponseEntity<String> response;

    try {
      addProductToCollection(product);
      response = new ResponseEntity<>("" + product.getProductId(), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return response;
  }

  private void addProductToCollection(Product product) {

  }

  /**
   * Delete a product from the collection
   *
   * @param id ID of the product to delete
   * @return 200 OK on success, 404 Not found on error
   */
  @DeleteMapping("/{id}")
  @Operation(hidden = true)
  public ResponseEntity<String> delete(@PathVariable int id) {
    ResponseEntity<String> response;
    if (removeProductFromCollection(id)) {
      response = new ResponseEntity<>(HttpStatus.OK);
    } else {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }

  private boolean removeProductFromCollection(int id) {
    return true;
  }
}
