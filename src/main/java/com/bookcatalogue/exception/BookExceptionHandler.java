package com.bookcatalogue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public final class BookExceptionHandler {

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<Object>
    handleBookNotFoundException(BookNotFoundException e) {
       BookException bookException = new BookException(
               e.getMessage(),
               HttpStatus.NOT_FOUND,
               ZonedDateTime.now(ZoneId.of("Z"))
       );
        return new ResponseEntity<>(bookException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object>
    customValidationErrorHandling(MethodArgumentNotValidException e) {
        BookException bookException = new BookException(
                "Validation Error " + e.getBindingResult()
                        .getFieldError().getDefaultMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(bookException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object>
    handleGlobalException(Exception e, WebRequest request) {
        BookException bookException = new BookException(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(bookException,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
