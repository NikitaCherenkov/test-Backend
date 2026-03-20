package com.example.demo.dto.request;

import com.example.demo.enums.Currency;
import com.example.demo.enums.Nds;
import com.example.demo.validation.ExistingCustomerCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @ExistingCustomerCode
    private String customerCode;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "NDS is required")
    private Nds nds;

    @NotNull(message = "Delivery place is required")
    private String placeDelivery;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDelivery;
}
