package com.example.demo.service;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.exceptions.CustomerCodeAlreadyExistException;
import com.example.demo.exceptions.CustomerHasChildrenException;
import com.example.demo.exceptions.CustomerHasLotsException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Customer;
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

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final LotRepository lotRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        String customerCode = request.getCustomerCode();
        if (customerCode != null && customerRepository.existsByCode(customerCode)) {
            throw new CustomerCodeAlreadyExistException("Customer with code " + customerCode + " already exists");
        }

        checkMainCustomerCode(request);

        Customer newCustomer = customerMapper.fromRequest(request);
        Customer savedCustomer = customerRepository.create(newCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    public CustomerResponse updateCustomer(Integer customerID, CustomerRequest request) {
        CustomerRecord existingRecord = getCustomerRecord(customerID);

        String oldCustomerCode = existingRecord.getCustomerCode();
        String newCustomerCode = request.getCustomerCode();
        if (!oldCustomerCode.equals(newCustomerCode)) {
            if (customerRepository.existsByCode(newCustomerCode)) {
                throw new CustomerCodeAlreadyExistException("Customer with code " + newCustomerCode + " already exists");
            }
            lotRepository.updateCustomerCode(oldCustomerCode, newCustomerCode);
            customerRepository.updateMainCustomerCode(oldCustomerCode, newCustomerCode);
        }

        checkMainCustomerCode(request);

        Customer customer = customerMapper.fromRequest(request);
        Customer updatedCustomer = customerRepository.update(existingRecord, customer);
        return customerMapper.toResponse(updatedCustomer);
    }

    public void deleteCustomer(Integer customerID) {
        CustomerRecord record = getCustomerRecord(customerID);

        String customerCode = record.getCustomerCode();
        if (lotRepository.existsByCustomerCode(customerCode)) {
            throw new CustomerHasLotsException(
                    "Cannot delete customer with code " + customerCode + " because they have associated lots"
            );
        }
        if (customerRepository.existsByCodeMain(customerCode)) {
            throw new CustomerHasChildrenException(
                    "Cannot delete customer with code " + customerCode + " because they have child customer(s)"
            );
        }

        customerRepository.delete(record);
    }

    public CustomerResponse getCustomerById(Integer customerID) {
        CustomerRecord record = getCustomerRecord(customerID);
        return customerMapper.toResponse(customerMapper.fromRecord(record));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.getAllCustomers()
                .stream()
                .map(customer -> customerMapper.toResponse(customerMapper.fromRecord(customer)))
                .collect(Collectors.toList());
    }

    private void checkMainCustomerCode(CustomerRequest request) {
        String codeMainCustomer = request.getCustomerCodeMain();
        if (codeMainCustomer != null &&
                !codeMainCustomer.trim().isEmpty() &&
                !customerRepository.existsByCode(codeMainCustomer)) {
            throw new IllegalArgumentException(
                    "Main customer with code " + codeMainCustomer + " does not exist"
            );
        }
    }

    private CustomerRecord getCustomerRecord(Integer customerID) {
        return customerRepository.findByID(customerID)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerID));
    }
}