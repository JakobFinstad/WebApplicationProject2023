package no.ntnu.idata2306.group6.entity.dto;

public class SignupDTO {
    private final String email;
    private final int phoneNumber;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final int age;


    public SignupDTO(String firstName, String lastName, String email, int phoneNumber, String password, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }


    public String getEmail() {
        return this.email;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public int getAge() {
        return this.age;
    }


}
