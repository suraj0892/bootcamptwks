package com.tw.bootcamp.bookshop.book.exceptions;

public class NoBooksFoundException extends Throwable {
    public NoBooksFoundException(String message) {
        super(message);
    }
}