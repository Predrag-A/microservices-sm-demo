package com.predrag.a.feedservice.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6727197723170077857L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
