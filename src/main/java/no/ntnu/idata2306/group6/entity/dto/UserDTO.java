package no.ntnu.idata2306.group6.entity.dto;

import com.sun.jna.StringArray;
import no.ntnu.idata2306.group6.entity.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int phoneNumber;
    //    private String roleName;
    private int age;

    private boolean isActive;
//    private String authority;
    private String[] roleSet = new String[5];

    public UserDTO setRoles(Stream<String> roles) {
        roleSet = roles.toArray(String[]::new);
        return this;
    }

    public String[] getRoles() {
        return this.roleSet;
    }

//    public String getAuthority() {
//        return this.authority;
//    }

    public void activateAccount() {
        this.isActive = true;
    }

    public void deActivateAccount() {
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }

//    public UserDTO setAuthority(int authority) {
//        if (authority == 1) {
//            this.authority = "ADMIN";
//        } else {
//            this.authority = "USER";
//        }
//        return this;
//    }

    public int getAge() {
        return this.age;
    }

    public UserDTO setAge() {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public UserDTO setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

//    public String getRoleName() {
//        return roleName;
//    }

//    public UserDTO setRoleName(String roleName) {
//        this.roleName = roleName.toUpperCase();
//        return this;
//    }
}
