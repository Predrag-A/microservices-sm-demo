package com.predrag.a.feedservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@Builder
@ToString
public class UserFeed {

    @Id
    private String id;

    @CreatedBy
    private String username;

    @CreatedDate
    private Instant createdAt;

    private String postId;
}
