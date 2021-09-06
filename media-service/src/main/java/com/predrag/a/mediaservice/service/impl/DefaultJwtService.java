package com.predrag.a.mediaservice.service.impl;


import com.predrag.a.mediaservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Service
@Slf4j
public class DefaultJwtService implements JwtService {

    private static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${config.jwt.secret.apiKey}")
    private String secret;

    @Override
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(final String token) {
        final List<Object> auths = extractClaim(token, claims -> claims.get(AUTHORITIES_CLAIM, List.class));
        return auths.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Boolean validateToken(final String token) {
        return !isTokenExpired(token);
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

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
}
