package com.predrag.a.authservice.web.filter;

import com.predrag.a.common.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTH_BEARER = "Bearer ";
    private static final String EXCEPTION_MSG = "Error while parsing jwt:";

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public JwtRequestFilter(final UserDetailsService userDetailsService, final JwtService jwtService) {
        this.userDetailsService = userDetailsService;
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

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(jwtService.validateToken(jwt, userDetails.getUsername()))) {
                    final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (final Exception exception) {
            log.error(EXCEPTION_MSG, exception);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
