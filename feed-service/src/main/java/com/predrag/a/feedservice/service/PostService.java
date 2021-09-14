package com.predrag.a.feedservice.service;

import com.predrag.a.feedservice.client.dto.PostResponse;

import java.util.List;

public interface PostService {

    List<PostResponse> findPostsIn(String token, List<String> ids);
}
