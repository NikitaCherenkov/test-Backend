package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotResponse {

    private Long id;

    private String lotName;

    private String customerCode;

    private BigDecimal price;

    private String currencyCode;

    private String ndsRate;

    private String placeDelivery;

    private LocalDate dateDelivery;
}