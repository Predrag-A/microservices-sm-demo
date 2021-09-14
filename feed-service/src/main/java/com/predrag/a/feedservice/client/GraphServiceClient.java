package com.predrag.a.feedservice.client;

import com.predrag.a.feedservice.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient("${dependencies.graph.service.url}")
public interface GraphServiceClient {

    @GetMapping("/users/{username}/followers")
    ResponseEntity<Set<UserResponse>> findFollowers(
            @RequestHeader("Authorization") String token,
            @PathVariable String username);
}
