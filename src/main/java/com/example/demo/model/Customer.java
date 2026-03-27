package com.example.demo.model;

import com.example.demo.enums.CustomerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Customer {

    private Integer id;
    private String customerCode;
    private String customerName;
    private String customerInn;
    private String customerKpp;
    private String customerLegalAddress;
    private String customerPostalAddress;
    private String customerEmail;
    private String customerCodeMain;
    private boolean organisation;
    private boolean person;

    public CustomerType getCustomerType() {
        if (organisation) return CustomerType.ORGANIZATION;
        if (person) return CustomerType.PERSON;

        throw new IllegalStateException("Customer type is not set");
    }

    public void setCustomerType(CustomerType type) {
        this.organisation = type == CustomerType.ORGANIZATION;
        this.person = type == CustomerType.PERSON;
    }
}
