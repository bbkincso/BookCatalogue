package com.bookcatalogue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(final String message) {
        super(message);
    }

    public BookNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
