package com.jaimayal.exception;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String message,
        Integer statusCode,
        LocalDateTime timestamp
) {
}
