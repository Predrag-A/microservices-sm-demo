package com.predrag.a.authservice.exception;

import java.io.Serial;

public class EmailAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3414780645822997363L;

    public EmailAlreadyExistsException(final String message) {
        super(message);
    }
}
