package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.NoBooksFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoBooksFoundErrorHandler {
    @ExceptionHandler({ NoBooksFoundException.class })
    public ResponseEntity<ErrorResponse> handleNoBooksFoundError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NO_CONTENT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
