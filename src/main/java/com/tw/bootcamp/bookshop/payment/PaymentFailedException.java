package com.tw.bootcamp.bookshop.payment;

public class PaymentFailedException extends Throwable {

    PaymentFailedException(String message){
        super(message);
    }
}
