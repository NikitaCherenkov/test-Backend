package com.example.demo.mapper;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.CustomerType;
import com.example.demo.model.Customer;
import nu.studer.sample.tables.records.CustomerRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organisation", source = "customerType", qualifiedByName = "toOrganisation")
    @Mapping(target = "person", source = "customerType", qualifiedByName = "toPerson")
    Customer fromRequest(CustomerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isOrganization", source = "organisation")
    @Mapping(target = "isPerson", source = "person")
    void toRecord(Customer customer, @MappingTarget CustomerRecord record);

    @Mapping(target = "organisation", source = "isOrganization")
    @Mapping(target = "person", source = "isPerson")
    @Mapping(target = "customerType", ignore = true)
    Customer fromRecord(CustomerRecord record);

    @Mapping(target = "customerType", source = "customer", qualifiedByName = "toCustomerType")
    CustomerResponse toResponse(Customer customer);

    @Named("toOrganisation")
    default boolean toOrganisation(String type) {
        return CustomerType.ORGANIZATION.name().equalsIgnoreCase(type);
    }

    @Named("toPerson")
    default boolean toPerson(String type) {
        return CustomerType.PERSON.name().equalsIgnoreCase(type);
    }

    @Named("toCustomerType")
    default String toCustomerType(Customer customer) {
        if (customer == null) {
            return null;
        }
        if (customer.isOrganisation()) {
            return CustomerType.ORGANIZATION.name();
        }
        if (customer.isPerson()) {
            return CustomerType.PERSON.name();
        }
        return null;
    }

    @AfterMapping
    default void afterFromRecord(@MappingTarget Customer customer, CustomerRecord record) {
        if (record.getIsOrganization()) {
            customer.setCustomerType(CustomerType.ORGANIZATION);
        } else if (record.getIsPerson()) {
            customer.setCustomerType(CustomerType.PERSON);
        }
    }
}
