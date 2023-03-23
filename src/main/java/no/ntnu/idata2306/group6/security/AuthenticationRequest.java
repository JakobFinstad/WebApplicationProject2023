package no.ntnu.idata2306.group6.security;

/**
 * Class for holding the data the user will send in the login request.
 */
public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest() {

    }

    /**
     * Constructor for the authentication request.
     *
     * @param username to the user
     * @param password to the user
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the username of the request.
     *
     * @return the request's username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the password of the request.
     *
     * @return the password of the request
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the username of the request.
     *
     * @param username the new username for the request
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password of the request.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
