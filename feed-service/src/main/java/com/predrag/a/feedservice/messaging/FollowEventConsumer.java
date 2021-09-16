package com.predrag.a.feedservice.messaging;

import com.predrag.a.common.messaging.FollowEventPayload;
import com.predrag.a.feedservice.service.FeedGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class FollowEventConsumer {

    private final FeedGeneratorService feedGeneratorService;

    @Autowired
    public FollowEventConsumer(final FeedGeneratorService feedGeneratorService) {
        this.feedGeneratorService = feedGeneratorService;
    }

    @Bean
    public Consumer<KStream<String, FollowEventPayload>> processFollowEvents() {
        return kstream -> kstream.foreach(this::processPayload);
    }

    private void processPayload(final String key, final FollowEventPayload payload) {
        log.info("Processing payload: [{}]", payload);

        switch (payload.eventType()) {
            case CREATED -> log.error("Created event type not yet supported");
            case UPDATED -> log.error("Update event type not yet supported");
            case DELETED -> feedGeneratorService.removeUnfollowedPostsFromFeed(payload.user(), payload.followedUser());
        }
    }
}
