package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.CoordinatesReq;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    @PostMapping("/stores")
    public ResponseEntity<List<StoresRes>>getNearStores(CoordinatesReq coordinatesReq) {
        return ResponseEntity.ok(storeService.getNearStores(coordinatesReq));
    }
}
