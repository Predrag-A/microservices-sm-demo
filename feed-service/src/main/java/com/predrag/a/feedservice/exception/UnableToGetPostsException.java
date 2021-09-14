package com.predrag.a.feedservice.exception;

import java.io.Serial;

public class UnableToGetPostsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8773286177822251399L;

    public UnableToGetPostsException(final String message) {
        super(message);
    }
}
