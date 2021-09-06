package com.predrag.a.postservice.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedBy
    private String username;

    @NonNull
    private String imageUrl;

    @NonNull
    private String caption;
}
