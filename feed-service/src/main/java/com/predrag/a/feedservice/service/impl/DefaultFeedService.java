package com.predrag.a.feedservice.service.impl;

import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.client.dto.SlicedResult;
import com.predrag.a.feedservice.constants.AppConstants;
import com.predrag.a.feedservice.exception.ResourceNotFoundException;
import com.predrag.a.feedservice.model.UserFeed;
import com.predrag.a.feedservice.repository.FeedRepository;
import com.predrag.a.feedservice.service.AuthService;
import com.predrag.a.feedservice.service.FeedService;
import com.predrag.a.feedservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class DefaultFeedService implements FeedService {

    private final FeedRepository feedRepository;
    private final AuthService authService;
    private final PostService postService;

    @Autowired
    public DefaultFeedService(final FeedRepository feedRepository,
                              final AuthService authService,
                              final PostService postService) {
        this.feedRepository = feedRepository;
        this.authService = authService;
        this.postService = postService;
    }

    @Override
    public SlicedResult<PostResponse> getUserFeed(final String username, final Optional<Integer> page) {

        log.info("Getting feed for user {} page {}", username, page);

        final PageRequest pageRequest = page
                .map(pageNumber -> PageRequest.of(pageNumber, AppConstants.PAGE_SIZE))
                .orElse(PageRequest.of(0, AppConstants.PAGE_SIZE));

        final Slice<UserFeed> userFeed = feedRepository.findByUsername(username, pageRequest);
        if (userFeed.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Feed not found for user %s", username));
        }

        final List<PostResponse> posts = getPosts(userFeed);

        return SlicedResult
                .<PostResponse>builder()
                .content(posts)
                .isLast(userFeed.isLast())
                .page(page.orElse(0))
                .build();
    }

    private List<PostResponse> getPosts(final Slice<UserFeed> userFeed) {

        final String token = authService.getAccessToken();

        final List<String> postIds = userFeed.getContent().stream()
                .map(UserFeed::getPostId)
                .toList();
        final List<PostResponse> posts = postService.findPostsIn(token, postIds);

        final List<String> usernames = posts.stream()
                .map(PostResponse::getUsername)
                .distinct()
                .toList();

        final Map<String, String> usersProfilePics =
                authService.getUserProfilePics(token, usernames);

        posts.forEach(post -> post.setUserProfilePic(
                usersProfilePics.getOrDefault(post.getUsername(), StringUtils.EMPTY)));

        return posts;
    }
}
