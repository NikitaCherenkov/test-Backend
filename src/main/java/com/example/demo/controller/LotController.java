package com.example.demo.controller;

import com.example.demo.dto.response.LotResponse;
import com.example.demo.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
