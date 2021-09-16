package com.predrag.a.graphservice.service.impl;

import com.predrag.a.graphservice.messaging.producer.FollowEventSender;
import com.predrag.a.graphservice.model.User;
import com.predrag.a.graphservice.repository.UserRepository;
import com.predrag.a.graphservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final FollowEventSender followEventSender;

    @Autowired
    public DefaultUserService(final UserRepository userRepository,
                              final FollowEventSender followEventSender) {
        this.userRepository = userRepository;
        this.followEventSender = followEventSender;
    }

    @Override
    public void createUser(final User user) {
        final String username = user.getUsername();
        userRepository.findByUsername(username)
                .ifPresentOrElse(existingUser -> log.error("User with username [{}] already exists", username),
                        () -> {
                            userRepository.save(user);
                            log.info("User [{}] created successfully", username);
                        });
    }

    @Override
    public void updateUser(final User user) {
        final String username = user.getUsername();
        userRepository.findByUsername(user.getUsername())
                .ifPresentOrElse(existingUser -> {
                            existingUser.setName(user.getName());
                            existingUser.setProfilePic(user.getProfilePic());
                            log.info("User [{}] updated successfully", username);
                        },
                        () -> log.error("User with username [{}] does not exist", username));
    }

    @Override
    public Boolean follow(final String username, final String usernameToFollow) {
        return userRepository.findByUsername(username)
                .map(user ->
                        userRepository.findByUsername(usernameToFollow).map(
                                userToFollow -> followInternal(user, userToFollow)
                        ).orElse(false))
                .orElse(false);
    }

    @Override
    public Boolean unfollow(final String username, final String usernameToUnfollow) {
        return userRepository.findByUsername(username)
                .map(user ->
                        userRepository.findByUsername(usernameToUnfollow).map(
                                userToUnfollow -> unfollowInternal(user, userToUnfollow)
                        ).orElse(false))
                .orElse(false);
    }

    @Override
    public Boolean isFollowing(final String usernameA, final String usernameB) {
        return userRepository.isFollowing(usernameA, usernameB);
    }

    @Override
    public Set<User> findFollowers(final String username) {
        return userRepository.findFollowers(username);
    }

    @Override
    public Set<User> findFollowing(final String username) {
        return userRepository.findFollowing(username);
    }

    private Boolean followInternal(final User user, final User userToFollow) {
        final Set<User> followers = user.getFollows();
        if (!followers.contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userRepository.save(user);
        } else {
            log.warn("User [{}] is already following [{}]", user.getUsername(), userToFollow.getUsername());
        }
        return true;
    }


    private Boolean unfollowInternal(final User user, final User userToUnfollow) {
        final String username = user.getUsername();
        final String usernameToUnfollow = userToUnfollow.getUsername();
        final Set<User> followers = user.getFollows();
        if (followers.contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userRepository.save(user);
            followEventSender.sendUnfollowEvent(username, usernameToUnfollow);
        } else {
            log.warn("User [{}] is already not following [{}]", username, usernameToUnfollow);
        }
        return true;
    }
}
