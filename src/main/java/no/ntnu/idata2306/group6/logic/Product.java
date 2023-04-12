package no.ntnu.idata2306.group6.logic;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

/**
 * A class representing different products in the store.
 *
 * @author group 6
 * @version 0.1
 */
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;
    @NotNull
    private int price;
    @NotNull
    private String imgURL;
    @NotNull
    private String name;
    @NotNull
    private String description;

    public Product(){

    }

    /**
     * Constructor for a product. Have a few fields for describing the product.
     *
     * @param price amount of currency that need to be paid in order to achieve this product
     * @param imgURL the relative url from the root to the image associated with the product
     * @param name of the product
     * @param description more indepth on how this product is structured
     */
    public Product (int id, int price, String imgURL, String name, String description) {
        setPrice(price);
        setImageURL(imgURL);
        setName(name);
        setDescription(description);
        setProductId(id);
    }

    private void setProductId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID cannot be zero or lower!");
        }
        this.id = id;
    }

    /**
     * Set the price of the object.
     *
     * @param price of the product
     * @throws IllegalArgumentException if the price is a negative number
     */
    public void setPrice (int price) throws IllegalArgumentException {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative values!");
        }
        this.price = price;
    }

    /**
     * Set the url for the item. Has to be relative to the root.
     *
     * @param relativeURL url for the product relative to the root
     * @throws IllegalArgumentException if the url is empty
     */
    private void setImageURL (String relativeURL) throws IllegalArgumentException {
        if (relativeURL == null || relativeURL.isEmpty()) {
            throw new IllegalArgumentException("Relative URL cannot be null!");
        }
        this.imgURL = relativeURL;
    }

    /**
     * Set the name of the product.
     *
     * @param name of the product, cannot be empty
     * @throws IllegalArgumentException if the name is empty
     */
    private void setName (String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        this.name = name;
    }

    /**
     * Set the description of the product.
     *
     * @param description of the product, cannot be empty
     */
    public void setDescription (String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        this.description = description;
    }

    public int getProductId() {
        return this.id;
    }

    /**
     * Get the price of the given product.
     *
     * @return the price of the product
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get the image url relative to the root of this product.
     *
     * @return url relative to the root
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * Get the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the product.
     *
     * @return description of the product
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if this object is a valid product
     *
     * @return True if the product is valid, false otherwise
     */
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.equals("");
    }
}
