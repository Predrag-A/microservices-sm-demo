package com.predrag.a.graphservice.messaging;

import com.predrag.a.common.enums.EventType;
import com.predrag.a.common.messaging.UserEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class UserEventConsumer {

    @Bean
    public Consumer<KStream<String, UserEventPayload>> processUserEvents() {
        return kstream -> kstream.
                filter(this::filterCreated)
                .foreach(this::processPayload);
    }

    private boolean filterCreated(final String key, final UserEventPayload payload) {
        return EventType.CREATED.equals(payload.eventType());
    }

    private void processPayload(final String key, final UserEventPayload payload) {
        log.info("User created: [{}]", payload);
    }
}
