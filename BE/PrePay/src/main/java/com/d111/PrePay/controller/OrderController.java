package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.respond.DetailHistoryRes;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/history")
    public ResponseEntity<List<OrderHistoryRes>> getOrderHistory(@RequestBody OrderHistoryReq orderHistoryReq) {
        return ResponseEntity.ok(orderService.getOrderHistory(orderHistoryReq));
    }

    @GetMapping("/history/{detailHistoryId}")
    public ResponseEntity<List<DetailHistoryRes>> getDetailHistory(@PathVariable long detailHistoryId) {
        return ResponseEntity.ok(orderService.getDetailHistory(detailHistoryId));
    }
}
