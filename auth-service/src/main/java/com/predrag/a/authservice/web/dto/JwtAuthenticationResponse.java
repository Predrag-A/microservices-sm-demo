package com.predrag.a.authservice.web.dto;

public record JwtAuthenticationResponse(String accessToken,
                                        String tokenType) {

    private static final String TOKEN_TYPE = "Bearer";
    
    public JwtAuthenticationResponse(final String accessToken) {
        this(accessToken, TOKEN_TYPE);
    }
}
