package com.predrag.a.feedservice.web.controller;

import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.client.dto.SlicedResult;
import com.predrag.a.feedservice.service.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController(final FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<SlicedResult<PostResponse>> getFeed(
            @PathVariable final String username,
            @RequestParam(required = false) final Optional<Integer> page) {

        log.info("Fetching feed for user {} current page {}",
                username, page.orElse(0));

        return ResponseEntity.ok(feedService.getUserFeed(username, page));
    }
}
