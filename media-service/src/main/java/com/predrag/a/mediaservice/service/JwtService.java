package com.predrag.a.mediaservice.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
     * Retrieves the authorities from the JWT token
     *
     * @param token JWT token
     * @return list of {@link SimpleGrantedAuthority}
     */
    List<SimpleGrantedAuthority> extractAuthorities(final String token);

    /**
     * Validates whether the given JWT token is still valid
     *
     * @param token JWT token
     * @return
     */
    Boolean validateToken(final String token);
}
