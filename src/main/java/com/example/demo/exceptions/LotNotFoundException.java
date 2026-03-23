package com.example.demo.exceptions;

public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(String message) {
        super(message);
    }
}
