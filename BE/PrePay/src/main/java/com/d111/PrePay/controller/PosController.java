package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderReq;
import com.d111.PrePay.service.OrderService;
import com.d111.PrePay.service.PosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<Long> makeOrder(@RequestBody OrderReq orderReq) {
        return ResponseEntity.ok(posService.makeOrder(orderReq));
    }
}
