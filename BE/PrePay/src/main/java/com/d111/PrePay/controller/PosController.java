package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.service.PosService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "주문 생성", description = "<b>long : userTeamId" +
            "<br>long : storeId" +
            "<br>boolean companyDinner ->회식이면 true" +
            "<br> List(DetailHistoryReq) details")
    public ResponseEntity<Long> makeOrder(@RequestBody OrderCreateReq orderCreateReq) {
        return ResponseEntity.ok(posService.makeOrder(orderCreateReq));
    }

    @PostMapping("/redisOrder")
    @Operation(summary = "redis 주문 생성", description = "<b>long : userTeamId" +
            "<br>long : storeId" +
            "<br>boolean companyDinner ->회식이면 true" +
            "<br> List(DetailHistoryReq) details")
    public ResponseEntity<Long> makeRedisOrder(@RequestBody OrderCreateReq orderCreateReq) {
        return ResponseEntity.ok(posService.makeRedisOrder(orderCreateReq));
    }
}
