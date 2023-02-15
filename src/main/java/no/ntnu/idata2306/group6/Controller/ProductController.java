package no.ntnu.idata2306.group6.Controller;

import no.ntnu.idata2306.group6.Repository.ProductRepository;
import no.ntnu.idata2306.group6.logic.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * REST API controller for product collection
 */
@RestController
@RequestMapping("/product")
public class ProductController {
  private Map<Integer, Product> products;
  private int latestId;
  private ProductRepository productRepository;

  public ProductController() {initializeData();}

  /**
   * Initialize dummy product data for the collection
   */
  private void initializeData() {
    latestId = 1;
    products = new HashMap<>();
    //addProductToCollection(new Product())
  }

}
