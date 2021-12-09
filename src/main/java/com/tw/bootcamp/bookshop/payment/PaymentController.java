package com.tw.bootcamp.bookshop.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity pay(@RequestBody PaymentRequest paymentRequest) throws PaymentFailedException{
        String message = paymentService.pay(paymentRequest);
        return new ResponseEntity(message, HttpStatus.ACCEPTED);
    }
}
