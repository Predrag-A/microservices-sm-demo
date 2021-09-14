package com.predrag.a.feedservice.client.dto;

public record UserSummaryResponse(String id,
                                  String username,
                                  String name,
                                  String profilePicture) {
}
