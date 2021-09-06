package com.predrag.a.authservice.service;

import java.util.Date;
import java.util.List;


public interface JwtService {

    /**
     * Retrieves username from the JWT token
     *
     * @param token JWT token
     * @return username
     */
    String extractUsername(final String token);

    /**
     * Retrieves expiration date from the JWT token
     *
     * @param token JWT token
     * @return expiration date
     */
    Date extractExpiration(final String token);

    /**
     * Creates a JWT token for the given username and authorities
     *
     * @param username    username
     * @param authorities authorities
     * @return JWT token
     */
    String generateToken(final String username, List<String> authorities);

    /**
     * Validates whether the given JWT token is still valid
     *
     * @param token    JWT token
     * @param username username param which should match the username in the JWT token
     * @return
     */
    Boolean validateToken(final String token, final String username);
}
