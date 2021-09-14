package com.predrag.a.feedservice.client.dto;

public record JwtAuthenticationResponse(String accessToken,
                                        String tokenType) {
}
