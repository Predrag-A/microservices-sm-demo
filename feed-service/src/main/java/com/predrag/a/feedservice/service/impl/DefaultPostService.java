package com.predrag.a.feedservice.service.impl;

import com.predrag.a.feedservice.client.PostServiceClient;
import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.exception.UnableToGetPostsException;
import com.predrag.a.feedservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DefaultPostService implements PostService {

    private final PostServiceClient postServiceClient;

    public DefaultPostService(final PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public List<PostResponse> findPostsIn(final String token, final List<String> ids) {
        final ResponseEntity<List<PostResponse>> response = postServiceClient.findPostsByIdIn(token, ids);
        final List<PostResponse> posts = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || posts == null) {
            final String exceptionMsg = String.format("Unable to get posts for ids [%s], %s",
                    ids, response.getStatusCode());
            log.error(exceptionMsg);
            throw new UnableToGetPostsException(exceptionMsg);
        }
        return posts;
    }
}
