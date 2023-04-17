package no.ntnu.idata2306.group6.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

@Schema(description = "Represents a category that describes a product")
@Entity
@Table(name = "category")
public class Category{

    private static final Logger LOGGER = Logger.getLogger(Category.class.getName());

    @Schema(description = "Unique id for the category")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Schema(description = "What the category is called")
    @NotNull
    @Column(nullable = false)
    private String categoryName;


    @ManyToMany //Implement function
    @NotNull
    @Column(nullable = false)
    private Set<Product> products = new LinkedHashSet<>();

    public Category(String categoryName) {
        try {
            this.categoryId = validateInt(categoryId, "categoryId");
            this.categoryName = validateString(categoryName, "categoryName");
        } catch (IllegalArgumentException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    /**
     * Empty constructor
     */
    public Category() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String newName) {
        try {
            this.categoryName = validateString(newName, "categoryName");
        } catch (IllegalArgumentException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * Check that an int is valid by checking that it is not less than 1
     *
     * @param integer to be checked if is valid
     * @param string the type of integer that is checked
     * @return integer if it is valid
     */
    private int validateInt(int integer, String string) {
        if(integer <= 0) {
            throw new IllegalArgumentException(string + "cannot be less than 1");
        }
        return integer;
    }

    /**
     * Check that a string is valid by checking that it is not null or empty
     *
     * @param s
     * @param string
     * @return
     */
    private String validateString(String s, String string) {
        if (s == null || string.isEmpty()) {
            throw new IllegalArgumentException(string + "cannot be null or empty");
        }
        return s;
    }
}
