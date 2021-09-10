package com.predrag.a.graphservice.service.impl;

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

    @Autowired
    public DefaultUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public boolean follow(final String username, final String usernameToFollow) {
        return userRepository.findByUsername(username)
                .map(user ->
                        userRepository.findByUsername(usernameToFollow).map(
                                userToFollow -> followInternal(user, userToFollow)
                        ).orElse(false))
                .orElse(false);
    }

    @Override
    public boolean isFollowing(final String usernameA, final String usernameB) {
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

    private boolean followInternal(final User user, final User userToFollow) {
        user.getFollows().add(userToFollow);
        userRepository.save(user);
        return true;
    }
}
