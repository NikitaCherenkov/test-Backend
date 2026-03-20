package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private final String displayName;

    private Currency(String displayName) {
        this.displayName = displayName;
    }
}