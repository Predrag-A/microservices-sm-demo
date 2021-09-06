package com.predrag.a.mediaservice.web.filter;

import com.predrag.a.mediaservice.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String FORMAT_ERROR = "Authorization header token [{}] has an incorrect signature or format.";
    private static final String TOKEN_EXPIRED = "Authorization header token [{}] has expired.";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTH_BEARER = "Bearer ";

    private final JwtService jwtService;

    @Autowired
    public JwtRequestFilter(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String username = null;
        String jwt = null;

        try {
            if (authHeader != null && authHeader.startsWith(AUTH_BEARER)) {
                jwt = authHeader.substring(7);
                username = jwtService.extractUsername(jwt);
            }

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && Boolean.TRUE.equals(jwtService.validateToken(jwt))) {
                final var authToken = new UsernamePasswordAuthenticationToken(username, null,
                        jwtService.extractAuthorities(jwt));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (final ExpiredJwtException exception) {
            log.warn(TOKEN_EXPIRED, authHeader, exception);
        } catch (final SignatureException | MalformedJwtException exception) {
            log.warn(FORMAT_ERROR, authHeader, exception);
        }

        filterChain.doFilter(request, response);
    }
}
