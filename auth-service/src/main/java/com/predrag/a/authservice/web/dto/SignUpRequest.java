package com.predrag.a.authservice.web.dto;

public record SignUpRequest(String name,
                            String username,
                            String password,
                            String email) {
}
