package no.ntnu.idata2306.group6.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.function.Function;

import java.util.Date;

/**
 * Utility class for handling jwt tokens.
 * Code from https://youtu.be/X80nJ5T7YpE
 */
@Component
public class JwtUtil {
    @Value("${jwt_secret_key}")
    private String SECRET_KEY;
    // Key inside JWT token where roles are stored
    private static final String ROLE_KEY = "roles";

    /**
     * Generate a token for the given user. Lasts one hour.
     *
     * @param userDetails details of the user
     * @return the jwts token
     */
    public String generateToken(UserDetails userDetails) {
        final long TIME_NOW = System.currentTimeMillis();
        final long MILLISECONDS_IN_HOUR = 60 * 60 * 1000;
        final long TIME_AFTER_ONE_HOUR = TIME_NOW + MILLISECONDS_IN_HOUR;

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(ROLE_KEY, userDetails.getAuthorities())
                .setIssuedAt(new Date(TIME_NOW))
                .setExpiration(new Date(TIME_AFTER_ONE_HOUR))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Extract the username out of the token.
     *
     * @param token that shall be decoded
     * @return username of the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validate the token.
     *
     * @param token that shall be validated
     * @param userDetails details of the user the token coexists with
     * @return true if token is valid else false
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    /**
     * Get the expiration date of the token.
     *
     * @param token that shall be decoded
     * @return the date of the expiration for the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract the claims in a token.
     *
     * @param token that shall be searched for
     * @param claimsResolver the information that shall be retrieved from the token
     * @return the claims
     * @param <T>
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all the claims from a token.
     *
     * @param token that information shall be extracted from
     * @return all the claims in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Check if the given token has expired.
     *
     * @param token the token that shall be validated
     * @return true if the token has not expired and false if it has
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
