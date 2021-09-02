package com.predrag.a.authservice.service.impl;

import com.predrag.a.authservice.exception.EmailAlreadyExistsException;
import com.predrag.a.authservice.exception.ResourceNotFoundException;
import com.predrag.a.authservice.exception.UsernameAlreadyExistsException;
import com.predrag.a.authservice.messaging.UserEventSender;
import com.predrag.a.authservice.model.Role;
import com.predrag.a.authservice.model.User;
import com.predrag.a.authservice.repository.UserRepository;
import com.predrag.a.authservice.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Slf4j
public class DefaultUserService implements UserService {

    private static final String RETRIEVING_USERS = "Retrieving all users";
    private static final String RETRIEVING_USER = "Retrieving user [{}]";
    private static final String REGISTERING_USER = "Registering user [{}]";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEventSender userEventSender;

    @Autowired
    public DefaultUserService(final UserRepository userRepository,
                              final PasswordEncoder passwordEncoder,
                              final UserEventSender userEventSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEventSender = userEventSender;
    }

    @Override
    public List<User> findAll() {
        log.info(RETRIEVING_USERS);
        return getUserRepository().findAll();
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        log.info(RETRIEVING_USER, username);
        return getUserRepository().findByUsername(username);
    }

    @Override
    public List<User> findByUsernameIn(final List<String> usernames) {
        return getUserRepository().findByUsernameIn(usernames);
    }

    @Override
    public User registerUser(final User user) {
        log.info(REGISTERING_USER, user.getUsername());
        if (getUserRepository().existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if (getUserRepository().existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        final HashSet<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(Role.USER);
        user.setActive(true);
        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        user.setRoles(defaultRoles);

        final User savedUser = getUserRepository().save(user);
        getUserEventSender().sendUserCreated(savedUser);

        return savedUser;
    }

    @Override
    public User updateProfilePicture(final String uri, final String id) {
        return userRepository
                .findById(id)
                .map(user -> handleProfilePictureUpdate(user, uri))
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("user id %s not found", id)));
    }

    private User handleProfilePictureUpdate(final User user, final String uri) {
        final String oldProfilePic = user.getUserProfile().getProfilePictureUrl();
        user.getUserProfile().setProfilePictureUrl(uri);
        final User savedUser = getUserRepository().save(user);

        getUserEventSender().sendUserUpdated(savedUser, oldProfilePic);

        return savedUser;
    }
}
