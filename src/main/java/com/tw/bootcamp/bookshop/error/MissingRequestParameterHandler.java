package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.InvalidRequestParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MissingRequestParameterHandler {
    @ExceptionHandler({InvalidRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleMissingRequestParameterError(InvalidRequestParameterException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
