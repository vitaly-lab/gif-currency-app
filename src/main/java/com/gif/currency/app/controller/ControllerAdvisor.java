package com.gif.currency.app.controller;

import com.gif.currency.app.exception.ErrorResponse;
import com.gif.currency.app.exception.NotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(NotFoundException notFoundException) {
        return ErrorResponse.builder()
                .message(notFoundException.getMessage())
                .status(BAD_REQUEST)
                .timestamp(now())
                .build();
    }
}
