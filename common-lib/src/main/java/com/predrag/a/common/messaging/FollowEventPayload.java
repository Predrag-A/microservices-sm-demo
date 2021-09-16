package com.predrag.a.common.messaging;

import com.predrag.a.common.enums.EventType;

public record FollowEventPayload(String user,
                                 String followedUser,
                                 EventType eventType) {
}
