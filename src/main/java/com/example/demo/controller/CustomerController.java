package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.service.CustomerService;
import com.example.demo.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final LotService lotService;

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerID}")
    public CustomerResponse getCustomer(@PathVariable Integer customerID) {
        return customerService.getCustomerById(customerID);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/{customerID}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Integer customerID,
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response = customerService.updateCustomer(customerID, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerID}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerID) {
        customerService.deleteCustomer(customerID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerID}/lots")
    public List<LotResponse> getCustomerLots(@PathVariable Integer customerID) {
        return lotService.getCustomerLots(customerID);
    }

    @PostMapping("/{customerID}/lots")
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponse createLot(
            @PathVariable Integer customerID,
            @Valid @RequestBody LotRequest request) {
        return lotService.create(customerID, request);
    }

    @PutMapping("/{customerID}/lots/{lotID}")
    public ResponseEntity<LotResponse> updateLot(
            @PathVariable Integer lotID,
            @Valid @RequestBody LotRequest request) {

        LotResponse response = lotService.update(lotID, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerID}/lots/{lotID}")
    public ResponseEntity<Void> deleteLot(@PathVariable Integer lotID) {
        lotService.delete(lotID);
        return ResponseEntity.noContent().build();
    }
}