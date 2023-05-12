package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class.getSimpleName());

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories.
     * HTTP Get to /category
     *
     * @return list of all categories currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "List of all categories currently stored in collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all ");
        Iterable<Category> categories = categoryService.getAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Get a specific category.
     *
     * @param id of the returned category
     * @return category with the given id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Integer id) {
        ResponseEntity<Category> response;
        Optional<Category> category = Optional.ofNullable(categoryService.findById(id));
        if (category.isPresent()) {
            response = new ResponseEntity<>(category.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new category.
     *
     * @param category data of the category to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody Category category) {
        ResponseEntity<String> response;

        try {
            addCategoryToCollection(category);
            response = new ResponseEntity<>("" + category.getCategoryId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove category from the collection.
     *
     * @param category to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeCategoryFromCollection(Category category) {
        boolean deleted = false;
        try {
            categoryService.remove(category);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the category with ID: " + category.getCategoryId() + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete a category from the collection.
     *
     * @param id ID of the category to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        Category categoryToDelete = categoryService.findById(id);
        if (removeCategoryFromCollection(categoryToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update a category in the repository.
     *
     * @param id of the category to update, from the URL
     * @param category new category data to store, from request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Category category) {
        ResponseEntity<String> response;
        try {
            updateCategory(id, category);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Add a category to collection.
     *
     * @param category the category to be added to collection if it is valid
     * @throws IllegalArgumentException
     */
    private void addCategoryToCollection(Category category) throws IllegalArgumentException {
        if (category == null || category.getCategoryId() < 0) {
            throw new IllegalArgumentException("Product is invalid");
        }
        categoryService.addCategory(category);
    }

    /**
     * Try to update a category with given ID. The category id must match the ID.
     *
     * @param id of the category
     * @param category the update category data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateCategory(int id, Category category) throws  IllegalArgumentException {
        Optional<Category> existingCategory = Optional.ofNullable(categoryService.findById(id));
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("No product with id " + id + " found");
        }
        if (category == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (category.getCategoryId() != id) {
            throw new IllegalArgumentException("Category ID in the URL does not match the ID " +
                    "in the ID in JSON data(request body)");
        }

        try {
            categoryService.update(id, category);
        } catch (Exception e) {
            logger.warn("Could not update category " + category.getCategoryId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update category " + category.getCategoryId());
        }
    }
}
