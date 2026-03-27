package com.example.demo.dto.response;

import com.example.demo.enums.CustomerType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private int id;

    private String customerCode;

    private String customerName;

    private String customerInn;

    private String customerKpp;

    private String customerLegalAddress;

    private String customerPostalAddress;

    private String customerEmail;

    private String customerCodeMain;

    private CustomerType customerType;
}