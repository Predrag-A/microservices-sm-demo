package com.predrag.a.graphservice.service;

import com.predrag.a.graphservice.model.User;

import java.util.Set;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    Boolean follow(String username, String usernameToFollow);

    Boolean unfollow(String username, String usernameToUnfollow);

    Boolean isFollowing(String usernameA, String usernameB);

    Set<User> findFollowers(String username);

    Set<User> findFollowing(String username);
}
