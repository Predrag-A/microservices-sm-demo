package com.predrag.a.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
    
    @Serial
    private static final long serialVersionUID = -9028258132559531790L;

    public AppException(final String message) {
        super(message);
    }
}
