package com.risi.mvc.data.demo.exception;

public class InsufficientPermissionException extends Exception {

    public InsufficientPermissionException() {
        super("You don't have sufficient permissions for this action.");
    }
}
