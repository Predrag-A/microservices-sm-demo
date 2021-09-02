package com.predrag.a.authservice.service.impl;


import com.predrag.a.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j
public class DefaultJwtService implements JwtService {

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
    public String generateToken(final String username) {
        final Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    @Override
    public Boolean validateToken(final String token, final String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
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

    private String createToken(final Map<String, Object> claims, final String subject) {
        final Date currentDate = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(currentDate)
                .setExpiration(DateUtils.addMinutes(currentDate, 30))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
