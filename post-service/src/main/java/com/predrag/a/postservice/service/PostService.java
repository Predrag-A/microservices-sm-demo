package com.predrag.a.postservice.service;

import com.predrag.a.postservice.model.Post;
import com.predrag.a.postservice.web.dto.PostRequest;

import java.util.List;

public interface PostService {
    
    Post createPost(PostRequest request, String username);

    void deletePost(final String postId, final String username);

    List<Post> findPostsByUsername(final String username);

    List<Post> findPostsByUserIds(final List<String> ids);
}
