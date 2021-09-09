package com.predrag.a.common.messaging;

import com.predrag.a.common.enums.EventType;

import java.time.Instant;


public record PostEventPayload(String id,
                               Instant createdAt,
                               Instant updatedAt,
                               String username,
                               String lastModifiedBy,
                               String imageUrl,
                               String caption,
                               EventType eventType) {
}
