package com.predrag.a.graphservice.repository;

import com.predrag.a.graphservice.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    String FIND_FOLLOWING = "MATCH (n:User {username: $0})-[:FOLLOWS]->(f:User) RETURN f";
    String FIND_FOLLOWERS = "MATCH (n:User {username: $0})<-[:FOLLOWS]-(f:User) RETURN f";
    String IS_FOLLOWING = "MATCH (n:User {username: $0}), (n1:User {username: $1}) RETURN EXISTS((n)-[:FOLLOWS]->(n1))";

    Optional<User> findByUsername(String username);

    @Query(IS_FOLLOWING)
    boolean isFollowing(String usernameA, String usernameB);

    @Query(FIND_FOLLOWING)
    Set<User> findFollowing(String username);

    @Query(FIND_FOLLOWERS)
    Set<User> findFollowers(String username);
}
