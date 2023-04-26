package no.ntnu.idata2306.group6.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class representing different products in the store.
 *
 * @author group 6
 * @version 0.1
 */
@Schema(description = "Represent a product", title = "Product")
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;
    @NotNull
    private int price;
    @NotNull
    private String name;

    @OneToMany(mappedBy = "product")
    private List<Info> infos = new ArrayList<>();

    public Product(){

    }

    /**
     * Constructor for a product. Have a few fields for describing the product.
     *
     * @param price amount of currency that need to be paid in order to achieve this product
     * @param name of the product
     */
    public Product (int id, int price, String name) {
        setPrice(price);
        setName(name);
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

//    /**
//     * Set the url for the item. Has to be relative to the root.
//     *
//     * @param relativeURL url for the product relative to the root
//     * @throws IllegalArgumentException if the url is empty
//     */
//    private void setImageURL (String relativeURL) throws IllegalArgumentException {
//        if (relativeURL == null || relativeURL.isEmpty()) {
//            throw new IllegalArgumentException("Relative URL cannot be null!");
//        }
//        this.imgURL = relativeURL;
//    }

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
        this.name = name.toLowerCase();
    }

  /*  *//**
     * Set the description of the product.
     *
     *//*
    public void setDescription (String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        this.description = description;
    }*/

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
//    public String getImgURL() {
//        return imgURL;
//    }

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
//    public String getDescription() {
//        return description;
//    }

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
