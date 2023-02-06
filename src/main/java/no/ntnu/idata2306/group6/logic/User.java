package no.ntnu.idata2306.group6.logic;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Integer> phonenumber;
    private ProductList productList;
    public User() {
        this.phonenumber = new ArrayList<>(8);

    }
    private void setFirstName(String firstName) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        this.firstName = firstName;
    }

    protected void setPhonenumber(List<Integer> phoneNumber) {
        if (phoneNumber.size()==8) {
            this.phonenumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("PhoneNumber must consist of 8 integers");
        }
    }

    private void setEmail(String email) {
        if(email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty!");
        } else {
            this.email = email;
        }
    }

    private void setLastName(String lastName) {
        if(lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null!");
        }
        this.lastName = lastName;
    }

    protected void setWishList(ProductList productList) {
        if (productList ==null){
            throw new IllegalArgumentException("Wishlist cannot be null!");
        }
        this.productList = productList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getPhonenumber() {
        return phonenumber;
    }

    public ProductList getWishList() {
        return productList;
    }
}
