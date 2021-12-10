package com.tw.bootcamp.bookshop.payment;

public class PaymentFailedException extends Throwable {

    private Object failureReason;

    PaymentFailedException(Object failureReason){
        super();
        this.failureReason = failureReason;
    }

    public Object getfailureMessage(){
        return this.failureReason;
    }
}
