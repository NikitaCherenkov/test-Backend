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

    @GetMapping("/{id}")
    public LotResponse getLot(@PathVariable int id) {
        return lotService.getLotById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponse createLot(@Valid @RequestBody LotRequest request) {
        return lotService.createLot(request);
    }

    @PutMapping("/{lotId}")
    public ResponseEntity<LotResponse> updateLot(
            @PathVariable int lotId,
            @Valid @RequestBody LotRequest request) {

        LotResponse response = lotService.updateLot(lotId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lotId}")
    public ResponseEntity<Void> deleteLot(@PathVariable int lotId) {
        lotService.deleteLot(lotId);
        return ResponseEntity.noContent().build();
    }
}
