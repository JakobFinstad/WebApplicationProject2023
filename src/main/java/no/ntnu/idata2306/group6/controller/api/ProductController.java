package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.entity.dto.ProductDTO;
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
   * Get all products.
   *
   * @param category optional category filter
   * @param model    the model object
   * @return list of all products currently stored in the collection or 500 code on error
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
      products = productService.getProductsByCategory(category);
    }

    logger.error("Getting all ");
    model.addAttribute("products", products);

    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  /**
   * Get all categories that one product has.
   *
   * @param id ID of the product
   * @return list of all categories currently linked to the product with the given ID
   */
  @GetMapping("/{id}/categories")
  @Operation(
          summary = "Get all categories that one product has",
          description = "List of all categories currently linked to the product with product id"
  )
  public ResponseEntity<Object> getAllCategories(@PathVariable int id) {

    Product product = productService.findById(id);
    Iterable<Category> categories;
    if (product == null) {
      categories = null;
    } else {
      categories = productService.getAllCategoriesByProduct(id);
    }

    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  /**
   * Get a specific product.
   *
   * @param id ID of the returned product
   * @return product with the given ID or status 404
   */
  @GetMapping("/{id}")
  @Operation(
          summary = "Get a specific product",
          description = "Retrieve a product with the specified ID"
  )
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
   * @param productDTO data of the product to add. ID will be ignored.
   * @return 201 Created on success and the new ID in the response body,
   * 400 Bad request if some data is missing or incorrect
   */
  @PostMapping()
  @Operation(deprecated = true)
  public ResponseEntity<String> add(@RequestBody ProductDTO productDTO) {
    ResponseEntity<String> response;

    try {
      Product product = new Product(productDTO.price(), productDTO.productName());
      addProductToCollection(product);
      response = new ResponseEntity<>("" + product.getProductId(), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return response;
  }

  /**
   * Delete a product from the collection.
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
      response = new ResponseEntity<>(HttpStatus.OK);
    } else {
      response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return response;
  }

  /**
   * Remove a product from the collection.
   *
   * @param product product to remove
   * @return true when the product with that ID is removed, false otherwise
   */
  private boolean removeProductFromCollection(Product product) {
    boolean deleted = false;
    try {
      productService.remove(product);
      deleted = true;
    } catch (DataAccessException e) {
      logger.warn("Could not delete product with ID " + product.getProductId() + ": " + e.getMessage());
    }
    return deleted;
  }

  /**
   * Update a product in the repository.
   *
   * @param id      ID of the product to update, from the URL
   * @param product new product data to store, from the request body
   * @return 200 OK on success, 400 Bad request on error
   */
  @PutMapping("/{id}")
  @Operation(
          summary = "Update a product",
          description = "Update a product with the specified ID"
  )
  public ResponseEntity<String> update(@PathVariable int id, @RequestBody Product product) {
    ResponseEntity<String> response;
    try {
      updateProduct(id, product);
      response = new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return response;
  }

  /**
   * Add a product to the collection.
   *
   * @param product the product to be added to the collection if it is valid
   * @throws IllegalArgumentException if the product is invalid
   */
  private void addProductToCollection(Product product) throws IllegalArgumentException {
    if (!product.isValid()) {
      throw new IllegalArgumentException("Product is invalid");
    }
    productService.addProduct(product);
  }

  /**
   * Try to update a product with the given ID. The product ID must match the ID.
   *
   * @param id      ID of the product
   * @param product the updated product data
   * @throws IllegalArgumentException if something goes wrong
   */
  private void updateProduct(int id, Product product) throws IllegalArgumentException {

    Optional<Product> existingProduct = Optional.ofNullable(productService.findById(id));
    if (existingProduct.isEmpty()) {
      throw new IllegalArgumentException("No product with ID " + id + " found");
    }
    if (product == null || !product.isValid()) {
      throw new IllegalArgumentException("Wrong data in the request body");
    }
    if (product.getProductId() != id) {
      throw new IllegalArgumentException("Product ID in the URL does not match the ID in the JSON data (request body)");
    }

    try {
      productService.update(id, product);
    } catch (Exception e) {
      logger.warn("Could not update product " + product.getProductId() + ": " + e.getMessage());
      throw new IllegalArgumentException("Could not update product " + product.getProductId());
    }
  }
}
