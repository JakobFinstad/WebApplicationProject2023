package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST API controller for product collection
 */
@Controller
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {
  private ProductService productService;

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class.getSimpleName());

  public ProductController(ProductService productService) {
    this.productService = productService;
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
  public ResponseEntity<Object> getAll(@RequestParam(required = false) String category, Model model) {
    Iterable<Product> products;
    if (category == null) {
      products = productService.getAll();
    } else {
      //products = productService.getAllByCategory(category);
      products = productService.getProductsByCategory(category);
    }

    logger.error("Getting all ");
    model.addAttribute("products", products);

    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @GetMapping("/{id}/categories")
  @Operation(
          summary = "Get all categories that one product has",
          description = "List of all categories currently linked to the product with product id"
  )
  public ResponseEntity<Object> getAllCategories(@PathVariable int id){

    Product product = productService.findById(id);
    Iterable<Category> categories;
    if (product == null){
      categories = null;
    } else {
      categories = productService.getAllCategoriesByProduct(id);
    }

    return new ResponseEntity<>(categories, HttpStatus.OK);
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
    Optional<Product> product = Optional.ofNullable(productService.findById(id));
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
  public ResponseEntity<String> add(@RequestBody Product product) {
    ResponseEntity<String> response;

    try {
      addProductToCollection(product);
      response = new ResponseEntity<>("" + product.getProductId(), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return response;
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
    Product productToDelete = productService.findById(id);
    if (removeProductFromCollection(productToDelete)) {
      response = new ResponseEntity<>(HttpStatus.FOUND);
    } else {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }

  /**
   * Remove Product from the collection
   *
   * @param product to remove
   * @return True when product with that ID is removed, false otherwise
   */
  private boolean removeProductFromCollection(Product product) {
    boolean deleted = false;
    try {
      productService.remove(product);
      deleted = true;
    } catch (DataAccessException e) {
      logger.warn("Could not delete product with ID" + product.getProductId() + ": " + e.getMessage());
    }
    return deleted;
  }

  /**
   * Update a product in the repository
   *
   * @param id   ID of the product to update, from the URL
   * @param product New product data to store, from request body
   * @return 200 OK on success, 400 Bad request on error
   */
  @PutMapping("/{id}")
  public ResponseEntity<String> update(@PathVariable int id, @RequestBody Product product) {
    ResponseEntity<String> response;
    try {
      updateProduct(id, product);
      response = new ResponseEntity<>(HttpStatus.FOUND);
    } catch (Exception e) {
      response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return response;
  }

  /**
   * Add a product to collection
   *
   * @param product the product to be added to collection if it is valid
   * @throws IllegalArgumentException
   */
  private void addProductToCollection(Product product) throws IllegalArgumentException {
    if (!product.isValid()) {
      throw new IllegalArgumentException("Product is invalid");
    }
    productService.addProduct(product);
  }

  /**
   * Try to update a product with given ID. The product id must match the id.
   *
   * @param id   ID of the product
   * @param product The updated product data
   * @throws IllegalArgumentException If something goes wrong.
   *                                  Error message can be used in HTTP response.
   */
  private void updateProduct(int id, Product product) throws IllegalArgumentException {

    Optional<Product> existingProduct = Optional.ofNullable(productService.findById(id));
    if (existingProduct.isEmpty()) {
      throw new IllegalArgumentException("No product with id " + id + " found");
    }
    if (product == null || !product.isValid()) {
      throw new IllegalArgumentException("Wrong data in request body");
    }
    if (product.getProductId() != id) {
      throw new IllegalArgumentException(
          "Product ID in the URL does not match the ID in JSON data (response body)");
    }

    try {
      productService.update(id, product);
    } catch (Exception e) {
      logger.warn("Could not update product " + product.getProductId() + ": " + e.getMessage());
      throw new IllegalArgumentException("Could not update product " + product.getProductId());
    }
  }
}

