package com.example.demo.exceptions;


public class CustomerHasLotsException extends RuntimeException {

    public CustomerHasLotsException(String message) {
        super(message);
    }
}