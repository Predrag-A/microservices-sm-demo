package com.predrag.a.graphservice.repository;

import com.predrag.a.graphservice.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
}
