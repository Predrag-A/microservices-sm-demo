package com.predrag.a.feedservice.repository;

import com.predrag.a.feedservice.model.UserFeed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends MongoRepository<UserFeed, String> {

    Slice<UserFeed> findByUsername(String username, Pageable pageable);

    List<UserFeed> deleteByPostId(String postId);

    List<UserFeed> deleteByUsernameAndPostIdIn(String username, List<String> postIds);
}
