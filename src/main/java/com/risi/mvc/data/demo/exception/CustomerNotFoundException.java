package com.risi.mvc.data.demo.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
