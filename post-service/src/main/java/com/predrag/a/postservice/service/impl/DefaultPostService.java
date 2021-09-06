package com.predrag.a.postservice.service.impl;

import com.predrag.a.postservice.messaging.PostEventSender;
import com.predrag.a.postservice.model.Post;
import com.predrag.a.postservice.repository.PostRepository;
import com.predrag.a.postservice.service.PostService;
import com.predrag.a.postservice.web.dto.PostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class DefaultPostService implements PostService {

    private static final String CREATING_POST = "Creating post [{}]";
    private static final String DELETING_POST = "Deleting post [{}] by user [{}]";
    private static final String POST_NOT_FOUND = "Post with ID [{}] and by user [{}] has not been found";
    private final PostEventSender postEventSender;
    private final PostRepository postRepository;

    @Autowired
    public DefaultPostService(final PostEventSender postEventSender,
                              final PostRepository postRepository) {
        this.postEventSender = postEventSender;
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(final PostRequest request, final String username) {
        log.info(CREATING_POST, request);

        final Instant now = Instant.now();
        Post post = Post.builder()
                .username(username)
                .lastModifiedBy(username)
                .imageUrl(request.imageUrl())
                .caption(request.caption())
                .createdAt(now)
                .updatedAt(now)
                .build();
        post = postRepository.insert(post);
        postEventSender.sendPostCreated(post);
        return post;
    }

    @Override
    public void deletePost(final String postId, final String username) {
        log.info(DELETING_POST, postId, username);
        postRepository.findByIdAndUsername(postId, username)
                .ifPresentOrElse(this::deletePost, () -> log.warn(POST_NOT_FOUND, postId, username));
    }

    @Override
    public List<Post> findPostsByUsername(final String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public List<Post> findPostsByUserIds(final List<String> ids) {
        return postRepository.findByIdInOrderByCreatedAtDesc(ids);
    }

    private void deletePost(final Post post) {
        postRepository.delete(post);
        postEventSender.sendPostDeleted(post);
    }
}
