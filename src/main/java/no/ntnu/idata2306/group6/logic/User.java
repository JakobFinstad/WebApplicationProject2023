package no.ntnu.idata2306.group6.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a user on the website.
 *
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int UserId;
    private boolean isActive = true;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private int phoneNumber;
    @NotNull
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new LinkedHashSet<>();
    private String imgURL;
//    private ProductList wishList;


    /**
     * Constructor for the user on the website.
     *
     * @param firstName of the user
     * @param lastName of the user
     * @param email that shall be connected with the account
     * @param phoneNumber number of the user
     * @param password of the user
     */
    public User(String firstName, String lastName, String email, int phoneNumber, String password) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setPassword(password);
    }

    public User() {

    }

    /**
     * Set the roles of the user.
     *
     * @param roles set of roles the user has access to
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
     * Get the roles of the user.
     * @return
     */
    public Set<Role> getRoles() {
        return this.roles;
    }

    /**
     * Check if the item is valid.
     *
     * @return false if either firstname or email is empty
     */
    public boolean isValid() {
        return !firstName.isEmpty() && !email.isEmpty();
    }

    /**
     * Get the password of the user.
     *
     * @return password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password of the user.
     *
     * @param password of the user
     */
    protected void setPassword(String password) {
        //TODO: Add regex checking in order to check the original password for the password rules
        this.password = password;
    }

    /**
     * Deactivate this user.
     */
    public void deActivateAccount() {
        this.isActive = false;
    }

    /**
     * Activate this user.
     */
    public void activateAccount() {
        this.isActive = true;
    }

    /**
     * Check if this user is active or not.
     *
     * @return true if the user is active and false if the user is not active
     */
    public boolean isActive() {
        return this.isActive;
    }
}
