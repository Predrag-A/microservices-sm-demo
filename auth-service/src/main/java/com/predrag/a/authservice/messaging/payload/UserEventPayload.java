package com.predrag.a.authservice.messaging.payload;

import com.predrag.a.authservice.enums.UserEventType;

public record UserEventPayload(String id,
                               String username,
                               String email,
                               String displayName,
                               String profilePictureUrl,
                               String oldProfilePictureUrl,
                               UserEventType userEventType) {
}
