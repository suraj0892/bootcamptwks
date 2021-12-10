package com.tw.bootcamp.bookshop.user.address;

public class NoAddressFoundException extends Exception{
    public NoAddressFoundException() {
        super();
    }

    public NoAddressFoundException(String message) {
        super(message);
    }
}
