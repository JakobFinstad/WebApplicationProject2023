package no.ntnu.idata2306.group6.entity.dto;

public class ProductDTO {
    private int productId;
    private String productName;
    private int price;

    public int productId() {
        return productId;
    }

    public ProductDTO setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public String productName() {
        return productName;
    }

    public ProductDTO setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int price() {
        return price;
    }

    public ProductDTO setPrice(int price) {
        this.price = price;
        return this;
    }
}
