package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
     *
     * @return list of all categories currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "List of all categories currently stored in the collection"
    )
    public ResponseEntity<Iterable<Category>> getAll() {
        logger.error("Getting all categories");
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
    @Operation(
            summary = "Get a specific category",
            description = "Get a category by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
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
     * Add a new category.
     *
     * @param category data of the category to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     *         400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(
            summary = "Add a new category",
            description = "Add a new category to the collection"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<String> add(@RequestBody Category category) {
        ResponseEntity<String> response;

        try {
            addCategoryToCollection(category);
            response = new ResponseEntity<>(String.valueOf(category.getCategoryId()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove category from the collection.
     *
     * @param category to remove
     * @return true when the category with that ID is removed, false otherwise
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
    @Operation(
            summary = "Delete a category",
            description = "Delete a category from the collection"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
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
     * @param id       of the category to update, from the URL
     * @param category new category data to store, from the request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a category",
            description = "Update a category in the collection"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
     * Add a category to the collection.
     *
     * @param category the category to be added to the collection if it is valid
     * @throws IllegalArgumentException if the category is invalid
     */
    private void addCategoryToCollection(Category category) throws IllegalArgumentException {
        if (category == null || category.getCategoryId() < 0) {
            throw new IllegalArgumentException("Category is invalid");
        }
        categoryService.addCategory(category);
    }

    /**
     * Try to update a category with the given ID. The category ID must match the ID.
     *
     * @param id       of the category
     * @param category the updated category data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateCategory(int id, Category category) throws IllegalArgumentException {
        Optional<Category> existingCategory = Optional.ofNullable(categoryService.findById(id));
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("No category with ID " + id + " found");
        }
        if (category == null) {
            throw new IllegalArgumentException("Wrong data in the request body");
        }
        if (category.getCategoryId() != id) {
            throw new IllegalArgumentException("Category ID in the URL does not match the ID " +
                    "in the JSON data (request body)");
        }

        try {
            categoryService.update(id, category);
        } catch (Exception e) {
            logger.warn("Could not update category " + category.getCategoryId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update category " + category.getCategoryId());
        }
    }
}
