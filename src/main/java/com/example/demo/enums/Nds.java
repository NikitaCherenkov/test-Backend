package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Nds {
    NDS_0("Без НДС", 0),
    NDS_18("18%", 18),
    NDS_20("20%", 20);

    private final String displayName;
    private final int rate;

    private Nds(String displayName, int rate) {
        this.displayName = displayName;
        this.rate = rate;
    }
}
