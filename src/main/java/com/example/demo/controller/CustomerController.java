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

    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable int id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable int id,
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/lots")
    public List<LotResponse> getCustomerLots(@PathVariable int id) {
        return lotService.getCustomerLots(id);
    }

    @PostMapping("/{id}/lots")
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponse createLot(
            @PathVariable int id,
            @Valid @RequestBody LotRequest request) {
        return lotService.createLot(id, request);
    }

    @PutMapping("/{customerId}/lots/{lotId}")
    public ResponseEntity<LotResponse> updateLot(
            @PathVariable int lotId,
            @Valid @RequestBody LotRequest request) {

        LotResponse response = lotService.updateLot(lotId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}/lots/{lotId}")
    public ResponseEntity<Void> deleteLot(@PathVariable int lotId) {
        lotService.deleteLot(lotId);
        return ResponseEntity.noContent().build();
    }
}