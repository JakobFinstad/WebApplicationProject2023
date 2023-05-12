package no.ntnu.idata2306.group6.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
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
    private int productId;
    @NotNull
    private int price;
    @NotNull
    private String productName;
    @ManyToMany
    @NotNull
    @Column(nullable = false)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Info> infos = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("subscriptions")
    private List<Subscription> subscriptions = new ArrayList<>();

    @Transient
    private Info info;

    public Product(){

    }

    /**
     * Constructor for a product. Have a few fields for describing the product.
     *
     * @param price amount of currency that need to be paid in order to achieve this product
     * @param productName of the product
     */
    public Product (int productId, int price, String productName) {
        setPrice(price);
        setProductName(productName);
        setProductId(productId);
        this.info = infos.get(0);
    }

    private void setProductId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID cannot be zero or lower!");
        }
        this.productId = id;
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
    private void setProductName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        this.productName = name.toLowerCase();
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
        return this.productId;
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
    public String getProductName() {
        String firstLetter = productName.substring(0,1).toUpperCase();
        String restOfProductName = productName.substring(1);

        return firstLetter + restOfProductName;
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
        return productName != null && !productName.equals("");
    }

    public List<Subscription> subscriptions() {
        return subscriptions;
    }

    public Product setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public Info getInfo() {
        return this.infos.get(0);
    }
}
