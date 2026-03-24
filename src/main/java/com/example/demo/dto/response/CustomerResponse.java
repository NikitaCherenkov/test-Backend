package com.example.demo.dto.response;

import com.example.demo.enums.CustomerType;
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

    private CustomerType customerType;

    public static CustomerResponse fromRecord(CustomerRecord record) {
        if (record == null) return null;

        return CustomerResponse.builder()
                .id(record.getId())
                .name(record.getCustomerName())
                .code(record.getCustomerCode())
                .inn(record.getCustomerInn())
                .kpp(record.getCustomerKpp())
                .legalAddress(record.getCustomerLegalAddress())
                .postalAddress(record.getCustomerPostalAddress())
                .email(record.getCustomerEmail())
                .codeMainCustomer(record.getCustomerCodeMain())
                .customerType(getCustomerType(record))
                .build();
    }

    private static CustomerType getCustomerType(CustomerRecord record) {
        if (record.getIsOrganization()) {
            return CustomerType.ORGANIZATION;
        }
        if (record.getIsPerson()) {
            return CustomerType.PERSON;
        }

        return null;
    }
}