package com.example.demo.exceptions;

public class CustomerCodeAlreadyExistException extends RuntimeException {
    public CustomerCodeAlreadyExistException(String message) {
        super(message);
    }

    public CustomerCodeAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}