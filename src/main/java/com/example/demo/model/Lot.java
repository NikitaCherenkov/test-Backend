package com.example.demo.model;

import com.example.demo.enums.Currency;
import com.example.demo.enums.Nds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Lot {

    private Integer id;
    private String lotName;
    private String customerCode;
    private BigDecimal price;
    private Currency currencyCode;
    private Nds ndsRate;
    private String placeDelivery;
    private LocalDate dateDelivery;
}
