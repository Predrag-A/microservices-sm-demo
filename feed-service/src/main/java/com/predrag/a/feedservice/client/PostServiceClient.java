package com.predrag.a.feedservice.client;

import com.predrag.a.feedservice.client.dto.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("${dependencies.post.service.url}")
public interface PostServiceClient {

    @PostMapping("/posts/in")
    ResponseEntity<List<PostResponse>> findPostsByIdIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> ids);
}
