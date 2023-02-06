package no.ntnu.idata2306.group6.logic;


/**
 * A class representing different products in the store.
 *
 * @author group 6
 * @version 0.1
 */
public class Product {
    private int price;
    private String imgURL;
    private String name;
    private String description;
    private int productNumber;

    /**
     * Constructor for a product. Have a few fields for describing the product.
     *
     * @param price amount of currency that need to be paid in order to achieve this product
     * @param imgURL the relative url from the root to the image associated with the product
     * @param name of the product
     * @param description more indepth on how this product is structured
     */
    public Product (int price, String imgURL, String name, String description, int productNumber) {
        setPrice(price);
        setImageURL(imgURL);
        setName(name);
        setDescription(description);
        setProductNumber(productNumber);
    }

    private void setProductNumber(int productNumber) {
        if (productNumber <= 0) {
            throw new IllegalArgumentException("Product number cannot be zero or lower!");
        }
        this.productNumber = productNumber;
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

    public int getProductNumber() {
        return this.productNumber;
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
}
