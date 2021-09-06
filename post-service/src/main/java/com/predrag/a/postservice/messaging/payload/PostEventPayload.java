package com.predrag.a.postservice.messaging.payload;

import com.predrag.a.postservice.enums.PostEventType;

import java.time.Instant;


public record PostEventPayload(String id,
                               Instant createdAt,
                               Instant updatedAt,
                               String username,
                               String lastModifiedBy,
                               String imageUrl,
                               String caption,
                               PostEventType eventType) {
}
