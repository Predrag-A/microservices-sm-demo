package com.predrag.a.feedservice.exception;

import java.io.Serial;

public class UnableToGetAccessTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7063733840131896432L;

    public UnableToGetAccessTokenException(final String message) {
        super(message);
    }
}
