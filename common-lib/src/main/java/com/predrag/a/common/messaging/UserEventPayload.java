package com.predrag.a.common.messaging;

import com.predrag.a.common.enums.EventType;

public record UserEventPayload(String id,
                               String username,
                               String email,
                               String displayName,
                               String profilePictureUrl,
                               String oldProfilePictureUrl,
                               EventType eventType) {
}
