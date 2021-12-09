package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.order.InvalidOrderRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidRequestHandler {
    @ExceptionHandler({InvalidOrderRequestException.class})
    public ResponseEntity<ErrorResponse> handleNoBooksFoundError(InvalidOrderRequestException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
