package com.predrag.a.postservice.web.controller;

import com.predrag.a.postservice.model.Post;
import com.predrag.a.postservice.service.PostService;
import com.predrag.a.postservice.web.dto.ApiResponse;
import com.predrag.a.postservice.web.dto.PostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;

    @Autowired
    public PostsController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody final PostRequest postRequest,
                                                  @AuthenticationPrincipal final String username) {
        final Post post = postService.createPost(postRequest, username);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/posts/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "Post created successfully", post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable("id") final String id,
                                             @AuthenticationPrincipal final String username) {
        postService.deletePost(id, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> findCurrentUserPosts(@AuthenticationPrincipal final String username) {

        return ResponseEntity.ok(postService.findPostsByUsername(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> findUserPosts(@PathVariable("username") final String username) {

        return ResponseEntity.ok(postService.findPostsByUsername(username));
    }

    @PostMapping("/in")
    public ResponseEntity<List<Post>> findPostsByIdIn(@RequestBody final List<String> ids) {

        return ResponseEntity.ok(postService.findPostsByUserIds(ids));
    }
}
