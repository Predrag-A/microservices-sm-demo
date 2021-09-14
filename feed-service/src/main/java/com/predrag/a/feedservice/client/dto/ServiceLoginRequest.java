package com.predrag.a.feedservice.client.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServiceLoginRequest {

    @Value("${security.service.username}")
    private String username;

    @Value("${security.service.password}")
    private String password;
}