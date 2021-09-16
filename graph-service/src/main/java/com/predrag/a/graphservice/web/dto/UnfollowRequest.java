package com.predrag.a.graphservice.web.dto;

public record UnfollowRequest(String username,
                              String userToUnfollow) {
}
