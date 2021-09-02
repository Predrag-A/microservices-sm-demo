package com.predrag.a.authservice.web.controller;

import com.predrag.a.authservice.exception.ResourceNotFoundException;
import com.predrag.a.authservice.model.DefaultUserDetails;
import com.predrag.a.authservice.model.Profile;
import com.predrag.a.authservice.model.User;
import com.predrag.a.authservice.service.UserService;
import com.predrag.a.authservice.web.dto.ApiResponse;
import com.predrag.a.authservice.web.dto.UserSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/me/picture")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProfilePicture(
            @RequestBody final String profilePicture,
            @AuthenticationPrincipal final DefaultUserDetails userDetails) {

        userService.updateProfilePicture(profilePicture, userDetails.getId());

        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Profile picture updated successfully"));
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") final String username) {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
                .ok(userService.findAll());
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSummary getCurrentUser(@AuthenticationPrincipal final DefaultUserDetails userDetails) {
        return convertTo(userDetails);
    }

    @GetMapping(value = "/summary/{username}")
    public ResponseEntity<?> getUserSummary(@PathVariable("username") final String username) {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @PostMapping(value = "/summary/in")
    public ResponseEntity<?> getUserSummaries(@RequestBody final List<String> usernames) {
        log.info("retrieving summaries for {} usernames", usernames.size());

        final List<UserSummary> summaries =
                userService
                        .findByUsernameIn(usernames)
                        .stream()
                        .map(this::convertTo)
                        .toList();

        return ResponseEntity.ok(summaries);
    }

    private UserSummary convertTo(final User user) {
        final Profile profile = user.getUserProfile();
        return new UserSummary(user.getId(),
                profile.getDisplayName(),
                user.getUsername(),
                profile.getProfilePictureUrl());
    }

    private UserSummary convertTo(final DefaultUserDetails user) {
        final Profile profile = user.getUserProfile();
        return new UserSummary(user.getId(),
                profile.getDisplayName(),
                user.getUsername(),
                profile.getProfilePictureUrl());
    }
}
