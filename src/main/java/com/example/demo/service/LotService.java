package com.example.demo.service;

import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.mapper.LotMapper;
import com.example.demo.model.Lot;
import com.example.demo.repositiory.CustomerRepository;
import com.example.demo.repositiory.LotRepository;
import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.CustomerRecord;
import nu.studer.sample.tables.records.LotRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotMapper lotMapper;
    private final CustomerRepository customerRepository;
    private final LotRepository lotRepository;

    public LotResponse create(Integer customerID, LotRequest request) {
        CustomerRecord customerRecord = customerRepository.findByID(customerID)
                .orElseThrow(() -> new ServiceException("Customer not found with id: " + customerID));

        request.setCustomerCode(customerRecord.getCustomerCode());
        return create(request);
    }

    public LotResponse create(LotRequest request) {
        if (request.getCustomerCode().trim().isEmpty()) {
            throw new ServiceException("Customer code is required");
        }

        String customerCode = request.getCustomerCode();
        if (!customerRepository.existsByCode(customerCode)) {
            throw new ServiceException("Customer not found with code: " + customerCode);
        }

        Lot newLot = lotMapper.fromRequest(request);
        Lot savedLot = lotRepository.create(newLot);
        return lotMapper.toResponse(savedLot);
    }

    public LotResponse update(Integer lotID, LotRequest request) {
        LotRecord existingRecord = getLotRecord(lotID);

        if (request.getCustomerCode().trim().isEmpty()) {
            throw new ServiceException("Customer code is required");
        }

        Lot lot = lotMapper.fromRequest(request);
        Lot updatedLot = lotRepository.update(existingRecord, lot);
        return lotMapper.toResponse(updatedLot);
    }

    public void delete(Integer lotID) {
        lotRepository.delete(getLotRecord(lotID));
    }

    public LotResponse getLotByID(Integer lotID) {
        return lotMapper.toResponse(lotMapper.fromRecord(getLotRecord(lotID)));
    }

    public List<LotResponse> getAllLots() {
        List<LotRecord> records = lotRepository.getAllLots();

        return records
                .stream()
                .map(lot -> lotMapper.toResponse(lotMapper.fromRecord(lot)))
                .collect(Collectors.toList());
    }

    public List<LotResponse> getCustomerLots(Integer customerID) {
        if (!customerRepository.existsById(customerID)) {
            throw new ServiceException("Customer not found with id: " + customerID);
        }

        List<LotRecord> records = lotRepository.findByCustomerCode(getCustomerRecord(customerID).getCustomerCode());

        return records
                .stream()
                .map(lot -> lotMapper.toResponse(lotMapper.fromRecord(lot)))
                .collect(Collectors.toList());
    }

    private LotRecord getLotRecord(Integer lotID) {
        return lotRepository.findByID(lotID)
                .orElseThrow(() -> new ServiceException("Customer not found with id: " + lotID));
    }

    private CustomerRecord getCustomerRecord(Integer customerId) {
        return customerRepository.findByID(customerId)
                .orElseThrow(() -> new ServiceException("Customer not found with id: " + customerId));
    }
}
