package com.tw.bootcamp.bookshop.book;

public class NoBooksFoundException extends Throwable {
    public NoBooksFoundException(String message) {
        super(message);
    }
}