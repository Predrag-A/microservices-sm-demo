package com.predrag.a.authservice.web.dto;

import com.predrag.a.authservice.enums.UserEventType;

public record UserEventPayload(String id,
                               String username,
                               String email,
                               String displayName,
                               String profilePictureUrl,
                               String oldProfilePictureUrl,
                               UserEventType userEventType) {
}
