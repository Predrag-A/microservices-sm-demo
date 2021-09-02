package com.predrag.a.authservice.messaging;

import com.predrag.a.authservice.model.User;

public interface UserEventSender {

    void sendUserCreated(User user);

    void sendUserUpdated(User user);

    void sendUserUpdated(User user, String oldPictureUrl);
}
