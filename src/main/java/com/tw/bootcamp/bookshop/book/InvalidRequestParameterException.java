package com.tw.bootcamp.bookshop.book;

public class InvalidRequestParameterException extends Throwable {
    public InvalidRequestParameterException(String message) {
        super(message);
    }
}