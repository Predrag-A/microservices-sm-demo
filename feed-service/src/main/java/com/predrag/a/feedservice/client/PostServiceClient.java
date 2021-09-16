package com.predrag.a.feedservice.client;

import com.predrag.a.feedservice.client.dto.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("${dependencies.post.service.url}")
public interface PostServiceClient {

    @PostMapping("/posts/in")
    ResponseEntity<List<PostResponse>> findPostsByIdIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> ids);

    @GetMapping("/posts/{username}")
    ResponseEntity<List<PostResponse>> findPostsByUsername(
            @RequestHeader("Authorization") String token,
            @PathVariable String username);
}
