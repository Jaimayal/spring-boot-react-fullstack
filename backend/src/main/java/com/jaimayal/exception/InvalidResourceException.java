package com.jaimayal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidResourceException extends RuntimeException {
    
    public InvalidResourceException(String message) {
        super(message);
    }
}
