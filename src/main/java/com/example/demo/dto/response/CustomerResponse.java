package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import nu.studer.sample.tables.records.CustomerRecord;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private int id;

    private String code;

    private String name;

    private String inn;

    private String kpp;

    private String legalAddress;

    private String postalAddress;

    private String email;

    private String codeMainCustomer;

    private boolean isOrganization;

    private boolean isPerson;

    public static CustomerResponse fromRecord(CustomerRecord record) {
        if (record == null) return null;

        return CustomerResponse.builder()
                .id(record.getId())
                .code(record.getCustomerCode())
                .inn(record.getCustomerInn())
                .kpp(record.getCustomerKpp())
                .legalAddress(record.getCustomerLegalAddress())
                .postalAddress(record.getCustomerPostalAddress())
                .email(record.getCustomerEmail())
                .codeMainCustomer(record.getCustomerCodeMain())
                .isOrganization(record.getIsOrganization())
                .isPerson(record.getIsPerson())
                .build();
    }
}