package com.predrag.a.postservice.messaging.impl;

import com.predrag.a.common.enums.EventType;
import com.predrag.a.common.messaging.PostEventPayload;
import com.predrag.a.postservice.messaging.PostEventSender;
import com.predrag.a.postservice.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultPostEventSender implements PostEventSender {

    private static final String POSTS_TOPIC = "posts-changed";

    private final KafkaTemplate<String, PostEventPayload> kafkaTemplate;

    @Autowired
    public DefaultPostEventSender(final KafkaTemplate<String, PostEventPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPostCreated(final Post post) {
        log.info("Sending message that post is created: [{}]", post);
        kafkaTemplate.send(POSTS_TOPIC, convertTo(post, EventType.CREATED));

    }

    @Override
    public void sendPostUpdated(final Post post) {
        log.info("Sending message that post is updated: [{}]", post);
        kafkaTemplate.send(POSTS_TOPIC, convertTo(post, EventType.UPDATED));
    }

    @Override
    public void sendPostDeleted(final Post post) {
        log.info("Sending message that post is deleted: [{}]", post);
        kafkaTemplate.send(POSTS_TOPIC, convertTo(post, EventType.DELETED));
    }

    private PostEventPayload convertTo(final Post post, final EventType eventType) {
        return new PostEventPayload(post.getId(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUsername(),
                post.getLastModifiedBy(),
                post.getImageUrl(),
                post.getCaption(),
                eventType);
    }
}
