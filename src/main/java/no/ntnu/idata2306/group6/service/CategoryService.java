package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for service logic to category.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    /**
     * Find category by id.
     *
     * @param id of the category that shall be returned
     * @return category if found, else null
     */
    public Category findById(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    /**
     * Add category to the collection.
     *
     * @param category that shall be added
     * @return true if the category was added and false in cas of failure
     */
    public boolean addCategory(Category category) {
        boolean added;
        if (category.getCategoryId() < 0 || categoryRepository.findById(category.getCategoryId()).orElse(null) == category) {
            added = false;
        } else {
            categoryRepository.save(category);
            added = true;
        }
        return added;
    }

    /**
     * Remove a category from the collection.
     *
     * @param category that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Category category) {
        boolean removed;

        try {
            categoryRepository.delete(category);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update a category in the collection.
     *
     * @param id of the category that shall be edited
     * @param category the new category that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Category category) {
        String errorMessage = null;

        Category existingCategory = findById(id);

        if (existingCategory == null) {
            errorMessage = "No existing category with id: " + id;
        } else if (category == null) {
            errorMessage = "New category is invalid";
        } else if (category.getCategoryId() != id) {
            errorMessage = "Id in URL does not match id in the category";
        }

        if (errorMessage == null) {
            categoryRepository.save(category);
        }

        return errorMessage;
    }

}
