package com.predrag.a.feedservice.service;

import com.predrag.a.feedservice.client.dto.PostResponse;

public interface FeedGeneratorService {

    void addToFeed(PostResponse postResponse);
}
