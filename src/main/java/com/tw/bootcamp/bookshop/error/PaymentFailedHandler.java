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
        com.tw.bootcamp.bookshop.payment.ErrorResponse error = (com.tw.bootcamp.bookshop.payment.ErrorResponse) ex.getfailureMessage();

        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, "Payment Failed.");

        for(int i=0; i < error.getDetails().length;i++){
            apiError.getErrors().put(String.valueOf(i), error.getDetails()[i]);
        }

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
