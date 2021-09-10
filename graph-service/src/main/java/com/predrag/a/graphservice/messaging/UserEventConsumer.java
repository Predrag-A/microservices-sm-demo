package com.predrag.a.graphservice.messaging;

import com.predrag.a.common.messaging.UserEventPayload;
import com.predrag.a.graphservice.model.User;
import com.predrag.a.graphservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class UserEventConsumer {

    private final UserService userService;

    @Autowired
    public UserEventConsumer(final UserService userService) {
        this.userService = userService;
    }

    @Bean
    public Consumer<KStream<String, UserEventPayload>> processUserEvents() {
        return kstream -> kstream.foreach(this::processPayload);
    }

    private void processPayload(final String key, final UserEventPayload payload) {
        log.info("Processing payload: [{}]", payload);

        final User user = convertPayload(payload);
        switch (payload.eventType()) {
            case CREATED -> userService.createUser(user);
            case UPDATED -> userService.updateUser(user);
            case DELETED -> log.error("User deletion not possible");
        }
    }

    private User convertPayload(final UserEventPayload payload) {
        return User
                .builder()
                .userId(payload.id())
                .username(payload.username())
                .name(payload.displayName())
                .profilePic(payload.profilePictureUrl())
                .build();
    }
}
