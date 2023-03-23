package no.ntnu.idata2306.group6.security;

/**
 * Class for generating an authentication response.
 */
public class AuthenticationResponse {
    private final String jwt;

    /**
     * Constructor for the authentication response.
     *
     * @param jwt generated response
     */
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Get the content of the response.
     *
     * @return the content of the response
     */
    public String getJwt() {
        return jwt;
    }
}
