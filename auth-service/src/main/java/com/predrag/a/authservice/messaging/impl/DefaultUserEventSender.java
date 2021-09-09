package com.predrag.a.authservice.messaging.impl;

import com.predrag.a.authservice.messaging.UserEventSender;
import com.predrag.a.authservice.model.Profile;
import com.predrag.a.authservice.model.User;
import com.predrag.a.common.enums.EventType;
import com.predrag.a.common.messaging.UserEventPayload;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Getter
@Service
@Slf4j
public class DefaultUserEventSender implements UserEventSender {

    private static final String USERS_TOPIC = "users-changed";

    private final KafkaTemplate<String, UserEventPayload> kafkaTemplate;

    @Autowired
    public DefaultUserEventSender(final KafkaTemplate<String, UserEventPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUserCreated(final User user) {
        log.info("Sending message that user is created: [{}]", user);
        getKafkaTemplate().send(USERS_TOPIC, convertTo(user, EventType.CREATED, null));
    }

    @Override
    public void sendUserUpdated(final User user) {
        log.info("Sending message that user is updated [{}]", user);
        getKafkaTemplate().send(USERS_TOPIC, convertTo(user, EventType.UPDATED, null));
    }

    @Override
    public void sendUserUpdated(final User user,
                                final String oldPictureUrl) {
        log.info("Sending message that user is updated: [{}] | [{}]", user, oldPictureUrl);
        getKafkaTemplate().send(USERS_TOPIC, convertTo(user, EventType.UPDATED, oldPictureUrl));
    }

    private UserEventPayload convertTo(final User user,
                                       final EventType eventType,
                                       final String oldProfilePictureUrl) {
        final Profile profile = user.getUserProfile();
        return new UserEventPayload(user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile.getDisplayName(),
                profile.getProfilePictureUrl(),
                oldProfilePictureUrl,
                eventType);
    }
}
