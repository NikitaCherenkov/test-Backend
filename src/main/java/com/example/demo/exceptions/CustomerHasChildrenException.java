package com.example.demo.exceptions;

public class CustomerHasChildrenException extends RuntimeException {

    public CustomerHasChildrenException(String message) {
        super(message);
    }
}