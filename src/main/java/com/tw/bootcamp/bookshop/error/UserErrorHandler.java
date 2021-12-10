package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.user.InvalidEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserErrorHandler {
    @ExceptionHandler({ InvalidEmailException.class })
    public ResponseEntity<ErrorResponse> handleCreateUserError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUserNotFoundError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
