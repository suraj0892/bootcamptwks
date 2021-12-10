package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.exceptions.NoBooksFoundException;
import com.tw.bootcamp.bookshop.user.address.NoAddressFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoContentFoundErrorHandler {
    @ExceptionHandler({NoBooksFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoBooksFoundError(NoBooksFoundException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NO_CONTENT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NoAddressFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoAddressFoundError(NoAddressFoundException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NO_CONTENT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
