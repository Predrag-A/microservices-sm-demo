package com.predrag.a.feedservice.service.impl;

import com.predrag.a.feedservice.client.GraphServiceClient;
import com.predrag.a.feedservice.client.PostServiceClient;
import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.client.dto.UserResponse;
import com.predrag.a.feedservice.exception.UnableToGetFollowersException;
import com.predrag.a.feedservice.exception.UnableToGetPostsException;
import com.predrag.a.feedservice.model.UserFeed;
import com.predrag.a.feedservice.repository.FeedRepository;
import com.predrag.a.feedservice.service.AuthService;
import com.predrag.a.feedservice.service.FeedGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DefaultFeedGeneratorService implements FeedGeneratorService {

    private final AuthService tokenService;
    private final GraphServiceClient graphClient;
    private final PostServiceClient postServiceClient;
    private final FeedRepository feedRepository;

    @Autowired
    public DefaultFeedGeneratorService(final AuthService tokenService,
                                       final GraphServiceClient graphClient,
                                       final PostServiceClient postServiceClient,
                                       final FeedRepository feedRepository) {
        this.tokenService = tokenService;
        this.graphClient = graphClient;
        this.postServiceClient = postServiceClient;
        this.feedRepository = feedRepository;
    }

    @Override
    public void addToFeed(final PostResponse post) {
        log.info("Adding post {} to feed for user {}",
                post.getUsername(), post.getId());

        final String token = tokenService.getAccessToken();

        final ResponseEntity<Set<UserResponse>> response = graphClient.findFollowers(token, post.getUsername());
        final Set<UserResponse> result = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || result == null) {

            final String message =
                    String.format("Unable to get followers for user %s", post.getUsername());

            log.warn(message);
            throw new UnableToGetFollowersException(message);

        }

        log.info("Found {} followers for user {}", result.size(), post.getUsername());

        result.stream()
                .map(user -> convertTo(user, post))
                .forEach(feedRepository::insert);
    }

    @Override
    public void deleteFromFeeds(final PostResponse post) {
        final String postId = post.getId();

        log.info("Deleting post [{}] from feeds", postId);
        log.info("Deleted [{}] feed entries", feedRepository.deleteByPostId(postId).size());
    }

    @Override
    public void removeUnfollowedPostsFromFeed(final String user, final String unfollowedUser) {
        log.info("Removing unfollowed user's posts [{}] from feed of user [{}]",
                unfollowedUser, user);

        final String token = tokenService.getAccessToken();

        final ResponseEntity<List<PostResponse>> response = postServiceClient.findPostsByUsername(token, unfollowedUser);
        final List<PostResponse> result = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || result == null) {

            final String message =
                    String.format("Unable to get posts for user %s", unfollowedUser);

            log.warn(message);
            throw new UnableToGetPostsException(message);
        }

        final List<String> postIds = result.stream()
                .map(PostResponse::getId)
                .toList();

        final List<UserFeed> deletedFeedEntries = feedRepository.deleteByUsernameAndPostIdIn(user, postIds);
        log.info("Removed {} feed entries for user {}", deletedFeedEntries.size(), user);
    }

    private UserFeed convertTo(final UserResponse user, final PostResponse post) {
        return UserFeed
                .builder()
                .username(user.getUsername())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
