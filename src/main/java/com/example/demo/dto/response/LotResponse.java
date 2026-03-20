package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import nu.studer.sample.tables.records.LotRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotResponse {

    private int id;

    private String name;

    private String customerCode;

    private BigDecimal price;

    private String currencyCode;

    private String ndsRate;

    private String placeDelivery;

    private LocalDate deliveryDate;

    public static LotResponse fromRecord(LotRecord record) {
        if (record == null) return null;

        return LotResponse.builder()
                .id(record.getId())
                .name(record.getLotName())
                .customerCode(record.getCustomerCode())
                .price(record.getPrice())
                .currencyCode(record.getCurrencyCode())
                .ndsRate(record.getNdsRate())
                .placeDelivery(record.getPlaceDelivery())
                .deliveryDate(record.getDateDelivery().toLocalDate())
                .build();
    }
}