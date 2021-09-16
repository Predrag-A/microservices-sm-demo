package com.predrag.a.feedservice.messaging;

import com.predrag.a.common.messaging.PostEventPayload;
import com.predrag.a.feedservice.client.dto.PostResponse;
import com.predrag.a.feedservice.service.FeedGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class PostEventConsumer {

    private final FeedGeneratorService feedGeneratorService;

    @Autowired
    public PostEventConsumer(final FeedGeneratorService feedGeneratorService) {
        this.feedGeneratorService = feedGeneratorService;
    }

    @Bean
    public Consumer<KStream<String, PostEventPayload>> processPostEvents() {
        return kstream -> kstream.foreach(this::processPayload);
    }

    private void processPayload(final String key, final PostEventPayload payload) {
        log.info("Processing payload: [{}]", payload);

        switch (payload.eventType()) {
            case CREATED -> feedGeneratorService.addToFeed(convertTo(payload));
            case UPDATED -> log.error("Update event type not supported");
            case DELETED -> feedGeneratorService.deleteFromFeeds(convertTo(payload));
        }
    }

    private PostResponse convertTo(final PostEventPayload payload) {
        return PostResponse
                .builder()
                .id(payload.id())
                .createdAt(payload.createdAt())
                .username(payload.username())
                .build();
    }
}
