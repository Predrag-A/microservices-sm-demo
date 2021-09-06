package com.predrag.a.postservice.messaging;

import com.predrag.a.postservice.model.Post;

public interface PostEventSender {

    void sendPostCreated(Post post);

    void sendPostUpdated(Post post);

    void sendPostDeleted(Post post);

}
