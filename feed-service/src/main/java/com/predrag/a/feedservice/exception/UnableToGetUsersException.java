package com.predrag.a.feedservice.exception;

import java.io.Serial;

public class UnableToGetUsersException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6016008259091404038L;

    public UnableToGetUsersException(final String message) {
        super(message);
    }
}
