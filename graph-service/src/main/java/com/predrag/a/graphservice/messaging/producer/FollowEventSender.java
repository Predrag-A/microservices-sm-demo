package com.predrag.a.graphservice.messaging.producer;

public interface FollowEventSender {

    void sendFollowEvent(String user, String followedUser);

    void sendUnfollowEvent(String user, String unfollowedUser);
}
