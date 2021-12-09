package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.payment.PaymentFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentFailedHandler {

    @ExceptionHandler({PaymentFailedException.class})
    public ResponseEntity<ErrorResponse> handlePaymentFailure(PaymentFailedException ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
