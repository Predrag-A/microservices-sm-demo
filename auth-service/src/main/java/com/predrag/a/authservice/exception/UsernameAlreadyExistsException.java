package com.predrag.a.authservice.exception;

import java.io.Serial;

public class UsernameAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2785354989514266641L;

    public UsernameAlreadyExistsException(final String message) {
        super(message);
    }
}
