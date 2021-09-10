package com.predrag.a.graphservice.web.controller;

import com.predrag.a.graphservice.model.User;
import com.predrag.a.graphservice.service.UserService;
import com.predrag.a.graphservice.web.dto.ApiResponse;
import com.predrag.a.graphservice.web.dto.FollowRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/followers")
    public ResponseEntity<ApiResponse> follow(@RequestBody final FollowRequest request) {

        final String username = request.username();
        final String userToFollow = request.userToFollow();

        log.info("Received a follow request from [{}] to follow [{}]",
                username,
                userToFollow);

        if (!userService.follow(username, userToFollow)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "One of the usernames provided is not valid"));
        }
        final ApiResponse response = new ApiResponse(true,
                String.format("User %s is following user %s", username, userToFollow));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{usernameA}/following/{usernameB}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable final String usernameA, @PathVariable final String usernameB) {
        log.info("Received request to check is user {} is following {}"
                , usernameA, usernameB);

        return ResponseEntity.ok(userService.isFollowing(usernameA, usernameB));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<Set<User>> findFollowers(@PathVariable final String username) {
        return ResponseEntity.ok(userService.findFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<Set<User>> findFollowing(@PathVariable final String username) {
        return ResponseEntity.ok(userService.findFollowing(username));
    }

//    @GetMapping("/paginated/{username}/followers")
//    public ResponseEntity<?> findFollowersPaginated(
//            @PathVariable final String username,
//            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) final int page,
//            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) final int size) {
//
//        return ResponseEntity.ok(userService.findPaginatedFollowers(username, page, size));
//    }
}
