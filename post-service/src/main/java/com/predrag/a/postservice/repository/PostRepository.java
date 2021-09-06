package com.predrag.a.postservice.repository;

import com.predrag.a.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUsernameOrderByCreatedAtDesc(final String username);

    List<Post> findByIdInOrderByCreatedAtDesc(final List<String> ids);

    Optional<Post> findByIdAndUsername(final String id, final String username);
}
