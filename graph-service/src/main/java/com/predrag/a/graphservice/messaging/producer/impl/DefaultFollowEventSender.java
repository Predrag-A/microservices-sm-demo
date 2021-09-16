package com.predrag.a.graphservice.messaging.producer.impl;

import com.predrag.a.common.enums.EventType;
import com.predrag.a.common.messaging.FollowEventPayload;
import com.predrag.a.graphservice.messaging.producer.FollowEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultFollowEventSender implements FollowEventSender {

    private static final String FOLLOWS_TOPIC = "follows";

    private final KafkaTemplate<String, FollowEventPayload> kafkaTemplate;

    @Autowired
    public DefaultFollowEventSender(final KafkaTemplate<String, FollowEventPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendFollowEvent(final String user, final String followedUser) {
        log.info("Sending message that user [{}] is now following [{}]", user, followedUser);
        kafkaTemplate.send(FOLLOWS_TOPIC, convertTo(user, followedUser, EventType.CREATED));
    }

    @Override
    public void sendUnfollowEvent(final String user, final String unfollowedUser) {
        log.info("Sending message that user [{}] has unfollowed [{}]", user, unfollowedUser);
        kafkaTemplate.send(FOLLOWS_TOPIC, convertTo(user, unfollowedUser, EventType.DELETED));
    }

    private FollowEventPayload convertTo(final String userA, final String userB, final EventType eventType) {
        return new FollowEventPayload(userA, userB, eventType);
    }
}
