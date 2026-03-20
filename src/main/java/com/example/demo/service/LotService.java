package com.example.demo.service;

import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.exceptions.CustomerNotFoundException;
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

    private final CustomerRepository customerRepository;
    private final LotRepository lotRepository;

    public List<LotResponse> getCustomerLots(int id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }

        List<LotRecord> records = lotRepository.findByCustomerCode(getCustomerRecord(id).getCustomerCode());

        return records
                .stream()
                .map(LotResponse::fromRecord)
                .collect(Collectors.toList());
    }

    public List<LotResponse> getAllLots() {
        List<LotRecord> records = lotRepository.getAllLots();

        return records
                .stream()
                .map(LotResponse::fromRecord)
                .collect(Collectors.toList());
    }

    public LotResponse createLot(int customerId, LotRequest request) {
        if (request.getCustomerCode() == null) request.setCustomerCode(getCustomerRecord(customerId).getCustomerCode());

        return storeDataRequest(lotRepository.createNewRecord(), request);
    }

    public LotResponse updateLot(int lotId, LotRequest request) {
        return storeDataRequest(getLotRecord(lotId), request);
    }

    public void deleteLot(int lotId) {
        lotRepository.deleteById(lotId);
    }

    private LotResponse storeDataRequest(LotRecord record, LotRequest request) {
        return LotResponse.fromRecord(lotRepository.save(setValues(record, request)));
    }

    private CustomerRecord getCustomerRecord(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    private LotRecord getLotRecord(int lotId) {
        return lotRepository.findById(lotId)
                .orElseThrow(() -> new CustomerNotFoundException("Lot not found with id: " + lotId));
    }

    private LotRecord setValues(LotRecord targetRecord, LotRequest sourceRequest) {
        targetRecord.setLotName(sourceRequest.getName());
        targetRecord.setCustomerCode(sourceRequest.getCustomerCode());
        targetRecord.setPrice(sourceRequest.getPrice());
        targetRecord.setCurrencyCode(sourceRequest.getCurrency().getDisplayName());
        targetRecord.setNdsRate(sourceRequest.getNds().getDisplayName());
        targetRecord.setPlaceDelivery(sourceRequest.getPlaceDelivery());
        targetRecord.setDateDelivery(sourceRequest.getDateDelivery().atStartOfDay());

        return targetRecord;
    }
}
