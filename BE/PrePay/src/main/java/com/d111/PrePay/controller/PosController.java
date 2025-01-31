package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.service.PosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;

    @PostMapping("/order")
    public ResponseEntity<Long> makeOrder(@RequestBody OrderCreateReq orderCreateReq) {
        return ResponseEntity.ok(posService.makeOrder(orderCreateReq));
    }
}
