package com.example.demo.service;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.CustomerType;
import com.example.demo.exceptions.CustomerCodeAlreadyExistException;
import com.example.demo.exceptions.CustomerHasLotsException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.repositiory.CustomerRepository;
import com.example.demo.repositiory.LotRepository;
import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.CustomerRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final LotRepository lotRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByCode(request.getCode())) {
            throw new CustomerCodeAlreadyExistException("Customer with code " + request.getCode() + " already exists");
        }

        checkMainCustomerCode(request);

        return storeDataRequest(customerRepository.createNewRecord(), request);
    }

    public CustomerResponse getCustomerById(int id) {
        return CustomerResponse.fromRecord(getCustomerRecord(id));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.getAllCustomers()
                .stream()
                .map(CustomerResponse::fromRecord)
                .collect(Collectors.toList());
    }

    public void deleteCustomer(int id) {
        CustomerRecord record = getCustomerRecord(id);

        if (lotRepository.existsByCustomerCode(record.getCustomerCode())) {
            throw new CustomerHasLotsException(
                    "Cannot delete customer with id " + id + " because they have associated lots"
            );
        }

        customerRepository.deleteById(id);
    }

    public CustomerResponse updateCustomer(int id, CustomerRequest request) {
        CustomerRecord existingRecord = getCustomerRecord(id);

        if (!existingRecord.getCustomerCode().equals(request.getCode()) &&
                customerRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Customer with code " + request.getCode() + " already exists");
        }

        checkMainCustomerCode(request);

        return storeDataRequest(existingRecord, request);
    }

    private CustomerResponse storeDataRequest(CustomerRecord record, CustomerRequest request) {
        return CustomerResponse.fromRecord(customerRepository.save(setValues(record, request)));
    }

    private CustomerRecord setValues(CustomerRecord targetRecord, CustomerRequest sourceRequest) {
        targetRecord.setCustomerCode(sourceRequest.getCode());
        targetRecord.setCustomerName(sourceRequest.getName());
        targetRecord.setCustomerInn(sourceRequest.getInn());
        targetRecord.setCustomerKpp(sourceRequest.getKpp());
        targetRecord.setCustomerLegalAddress(sourceRequest.getLegalAdress());
        targetRecord.setCustomerPostalAddress(sourceRequest.getPostalAdress());
        targetRecord.setCustomerEmail(sourceRequest.getEmail());
        targetRecord.setCustomerCodeMain(sourceRequest.getCodeMainCustomer());

        switch (sourceRequest.getCustomerType()) {
            case CustomerType.ORGANIZATION:
                targetRecord.setIsOrganization(true);
                targetRecord.setIsPerson(false);
                break;
            case CustomerType.PERSON:
                targetRecord.setIsOrganization(false);
                targetRecord.setIsPerson(true);
                break;
        }

        return targetRecord;
    }

    private void checkMainCustomerCode(CustomerRequest request) {
        if (request.getCodeMainCustomer() != null &&
                !request.getCodeMainCustomer().trim().isEmpty() &&
                !customerRepository.existsByCode(request.getCodeMainCustomer())) {
            throw new IllegalArgumentException(
                    "Main customer with code " + request.getCodeMainCustomer() + " does not exist"
            );
        }
    }

    private CustomerRecord getCustomerRecord(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }
}