package com.predrag.a.feedservice.service;

import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.client.dto.SlicedResult;

import java.util.Optional;

public interface FeedService {

    SlicedResult<PostResponse> getUserFeed(String username, Optional<Integer> page);
}
