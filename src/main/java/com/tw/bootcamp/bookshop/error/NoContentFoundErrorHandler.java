package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.exceptions.NoBooksFoundException;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.NoAddressFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class NoContentFoundErrorHandler {
    @ExceptionHandler({NoBooksFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoBooksFoundError(NoBooksFoundException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NO_CONTENT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NoAddressFoundException.class})
    public ResponseEntity<List<Address>> handleNoAddressFoundError(NoAddressFoundException ex) {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
    }
}
