package com.jaimayal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidResourceUpdatesException extends RuntimeException {
    
    public InvalidResourceUpdatesException(String message) {
        super(message);
    }
}
