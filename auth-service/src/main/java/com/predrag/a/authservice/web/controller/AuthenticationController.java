package com.predrag.a.authservice.web.controller;

import com.predrag.a.authservice.exception.BadRequestException;
import com.predrag.a.authservice.exception.EmailAlreadyExistsException;
import com.predrag.a.authservice.exception.UsernameAlreadyExistsException;
import com.predrag.a.authservice.model.Profile;
import com.predrag.a.authservice.model.User;
import com.predrag.a.authservice.service.UserService;
import com.predrag.a.authservice.web.dto.ApiResponse;
import com.predrag.a.authservice.web.dto.JwtAuthenticationResponse;
import com.predrag.a.authservice.web.dto.LoginRequest;
import com.predrag.a.authservice.web.dto.SignUpRequest;
import com.predrag.a.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService tokenProvider;

    @PostMapping
    public ResponseEntity<?> authenticateUser(final LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        final String jwt = tokenProvider.generateToken(loginRequest.username(), authorities);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PutMapping
    public ResponseEntity<?> createUser(final SignUpRequest payload) {
        log.info("Creating user {}", payload.username());


        final User user = new User();
        user.setUsername(payload.username());
        user.setEmail(payload.email());
        user.setPassword(payload.password());
        user.setUserProfile(Profile.builder().displayName(payload.name()).build());

        try {
            userService.registerUser(user);
        } catch (final UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }
}
