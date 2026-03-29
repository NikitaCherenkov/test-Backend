package com.example.demo.controller;

import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lots")
public class LotController {

    private final LotService lotService;

    @GetMapping
    public List<LotResponse> getAllLots() {
        return lotService.getAllLots();
    }

    @GetMapping("/{lotID}")
    public LotResponse getLot(@PathVariable Long lotID) {
        return lotService.getLotByID(lotID);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponse createLot(@Valid @RequestBody LotRequest request) {
        return lotService.create(request);
    }

    @PutMapping("/{lotID}")
    public ResponseEntity<LotResponse> updateLot(
            @PathVariable Long lotID,
            @Valid @RequestBody LotRequest request) {

        LotResponse response = lotService.update(lotID, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lotID}")
    public ResponseEntity<Void> deleteLot(@PathVariable Long lotID) {
        lotService.delete(lotID);
        return ResponseEntity.noContent().build();
    }
}
