package com.tw.bootcamp.bookshop.book.exceptions;

public class InvalidRequestParameterException extends Throwable {
    public InvalidRequestParameterException(String message) {
        super(message);
    }
}