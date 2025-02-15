package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.security.dto.CustomUserDetails;
import com.d111.PrePay.service.PosService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Long> makeOrder(@RequestBody OrderCreateReq orderCreateReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(posService.makeOrder(orderCreateReq,email));
    }
}
