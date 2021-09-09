package com.predrag.a.graphservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Data
@Builder
@Node
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String username;
    private String name;
    private String profilePic;

    @Relationship(type = "DIRECTED", direction = Relationship.Direction.OUTGOING)
    private Set<User> follows;
}