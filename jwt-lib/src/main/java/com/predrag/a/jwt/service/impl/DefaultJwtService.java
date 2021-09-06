package com.predrag.a.jwt.service.impl;


import com.predrag.a.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.util.function.Function;


public class DefaultJwtService implements JwtService {

    private static final String AUTHORITIES_CLAIM = "authorities";

    private final String secret;

    public DefaultJwtService(final String secret) {
        this.secret = secret;
    }

    @Override
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public List<String> extractAuthorities(final String token) {
        return extractClaim(token, claims -> claims.get(AUTHORITIES_CLAIM, List.class));
    }

    @Override
    public String generateToken(final String username, final List<String> authorities) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_CLAIM, authorities);
        return createToken(claims, username);
    }

    @Override
    public Boolean validateToken(final String token, final String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    @Override
    public Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

    }

    private String createToken(final Map<String, Object> claims, final String subject) {
        final Date currentDate = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(currentDate)
                .setExpiration(addMinutes(currentDate, 30))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public static Date addMinutes(final Date date, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            final Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MINUTE, amount);
            return c.getTime();
        }
    }
}
