package no.ntnu.idata2306.group6.security;

/**
 * Class for generating an authentication response.
 */
public class AuthenticationResponse {
    private String jwt;



    /**
     * Get the content of the response.
     *
     * @return the content of the response
     */
    public String getJwt() {
        return jwt;
    }

    public AuthenticationResponse setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }
}
