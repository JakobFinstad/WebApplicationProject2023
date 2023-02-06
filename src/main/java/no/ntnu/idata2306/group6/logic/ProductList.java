package no.ntnu.idata2306.group6.logic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductList {
    private List<Product> productList;
    private String type;

    public ProductList(String type) {
        this.productList = new ArrayList<>();
        setType(type);
    }

    private void setType(String type) {
        if (type.isEmpty()){
            throw new IllegalArgumentException("Type cannot be null!");
        }
        this.type = type;
    }

    public void addProduct(Product product) {
        if (productList.contains(product)){
            throw new UnsupportedOperationException("Product allready in list.");
        }
        productList.add(product);
    }

    public Product removeProduct(Product product) {
        if (!productList.contains(product)) {
            throw new NullPointerException("No such product in list.");
        }
        Product removedProduct = product;
        productList.remove(product);
        return removedProduct;
    }

    public Iterator<Product> getIterator() {
        return productList.iterator();
    }

    public String getType() {
        return this.type;
    }

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

    public int getIndexByProduct(Product product) {
        if (!productList.contains(product) && product == null){
            throw new NullPointerException("No such item in directory.");
        }
        return productList.indexOf(product);
    }
}
