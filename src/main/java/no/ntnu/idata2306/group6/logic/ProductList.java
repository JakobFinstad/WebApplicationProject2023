package no.ntnu.idata2306.group6.logic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class representing a list of product. The class have some basic methods
 * for manipulating the list.
 *
 * @author group 6
 * @version 06.02.2023
 */
public class ProductList {
    private List<Product> productList;
    private String type;

    /**
     * Constructor for the product list.
     *
     * @param type description of what kind of list this object is
     */
    public ProductList(String type) {
        this.productList = new ArrayList<>();
        setType(type);
    }

    /**
     * Set the type of the list.
     *
     * @param type of the list
     */
    private void setType(String type) {
        if (type.isEmpty()){
            throw new IllegalArgumentException("Type cannot be null!");
        }
        this.type = type;
    }

    /**
     * Add a product to the list.
     *
     * @param product that shall be added
     */
    public void addProduct(Product product) {
        if (productList.contains(product)){
            throw new UnsupportedOperationException("Product allready in list.");
        }
        productList.add(product);
    }

    /**
     * Remove a product from the list.
     *
     * @param product the product that shall be removed from the list
     * @return the product that got removed
     */
    public Product removeProduct(Product product) {
        if (!productList.contains(product)) {
            throw new NullPointerException("No such product in list.");
        }
        Product removedProduct = product;
        productList.remove(product);
        return removedProduct;
    }

    /**
     * Get iterator of the list.
     *
     * @return iterator of the productlist
     */
    public Iterator<Product> getIterator() {
        return productList.iterator();
    }

    /**
     * Get the type of the list.
     *
     * @return description of what the purpose of the list is
     */
    public String getType() {
        return this.type;
    }

    /**
     * Find product by productnumber.
     *
     * @param productNumber of the product that shall be found
     * @return the product of found, else return null
     */
    public Product searchByProductNumber (int productNumber) {
        if (productNumber <= 0) {
            throw new IllegalArgumentException("Product number have to be positive");
        }
        boolean searching = true;
        Product foundProduct = null;
        Iterator<Product> it = getIterator();
        while (searching && it.hasNext()) {
            if (it.next().getProductNumber() == productNumber) {
                foundProduct = it.next();
                searching = false;
            }
        }

        return foundProduct;
    }

    /**
     * Get the index(position) of a product in the list.
     *
     * @param product the product that shall be searched for
     * @return the position of the product
     */
    public int getIndexByProduct(Product product) {
        if (!productList.contains(product) && product == null){
            throw new NullPointerException("No such item in directory.");
        }
        return productList.indexOf(product);
    }
}
