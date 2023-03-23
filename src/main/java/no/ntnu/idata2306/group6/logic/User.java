package no.ntnu.idata2306.group6.logic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a user on the website.
 *
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    @NotNull
    private int UserId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private int phoneNumber;
    private String imgURL;
//    private ProductList wishList;

    public User() {

    }

    /**
     * Constructor for the user on the website.
     *
     * @param firstName of the user
     * @param lastName of the user
     * @param email that shall be connected with the account
     * @param phoneNumber number of the user
     */
    public User(String firstName, String lastName, String email, int phoneNumber) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
    }

    /**
     * Set the firstname of the user.
     *
     * @param firstName of the user
     */
    private void setFirstName(String firstName) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        this.firstName = firstName;
    }

    /**
     * Set the phone number of the user.
     *
     * @param phoneNumber number that shall be connected with the account
     */
    protected void setPhoneNumber(int phoneNumber) {
        if (phoneNumber > 00000000 && 99999999 > phoneNumber ) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("PhoneNumber must consist of 8 integers");
        }
    }

    /**
     * Link a email to the user.
     *
     * @param email that shall be connected
     */
    private void setEmail(String email) {
        if(email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty!");
        } else {
            this.email = email;
        }
    }

    /**
     * Set the last name of the user.
     *
     * @param lastName of the user
     */
    private void setLastName(String lastName) {
        if(lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null!");
        }
        this.lastName = lastName;
    }

    /**
     * Set the image URL of the user.
     *
     * @param imgURL of the user
     */
    private void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    /**
     * Set the wishlist of the user.
     *
     * @param productList wishlist of the user
     */
    /*protected void setWishList(ProductList productList) {
        if (productList ==null){
            throw new IllegalArgumentException("Wishlist cannot be null!");
        }
        this.wishList = productList;
    }*/

    /**
     * Get the id of the user.
     *
     * @return id of the user
     */
    public int getUserId() {
        return this.UserId;
    }

    /**
     * Get the first name of the user.
     *
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user.
     *
     * @return last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the email linked to the account.
     *
     * @return email linked to the account
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the phone number of the user.
     *
     * @return phonenumber
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the image URL of the user.
     *
     * @return image URL
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * Get the wishlist of the user.
     *
     * @return list with product the user show interest in
     */
//    public ProductList getWishList() {
//        return wishList;
//    }

    /**
     * Check if the item is valid.
     *
     * @return false if either firstname or email is empty
     */
    public boolean isValid() {
        return !firstName.isEmpty() && !email.isEmpty();
    }
}
