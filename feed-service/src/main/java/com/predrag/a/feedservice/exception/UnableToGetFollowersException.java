package com.predrag.a.feedservice.exception;

import java.io.Serial;

public class UnableToGetFollowersException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3349423883999842714L;

    public UnableToGetFollowersException(final String message) {
        super(message);
    }
}
