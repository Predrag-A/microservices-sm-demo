package com.predrag.a.feedservice.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String username;
}
