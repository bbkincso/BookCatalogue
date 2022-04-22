package com.bookcatalogue.exception;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

public final class BookException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public BookException(String m, HttpStatus s,
                         ZonedDateTime t) {
        this.message = m;
        this.httpStatus = s;
        this.timestamp = t;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
