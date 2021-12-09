package com.tw.bootcamp.bookshop.order;

public class InvalidOrderRequestException extends Throwable {
    public InvalidOrderRequestException(String message) {
        super(message);
    }
}
