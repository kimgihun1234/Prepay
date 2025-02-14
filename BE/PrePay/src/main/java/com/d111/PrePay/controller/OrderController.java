package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.request.RefundRequestCreateReq;
import com.d111.PrePay.dto.respond.DetailHistoryRes;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.security.dto.CustomUserDetails;
import com.d111.PrePay.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/history")
    @Operation(summary = "주문내역", description = "<b>long : teamId<br>" +
            "long : storeId")
    public ResponseEntity<List<OrderHistoryRes>> getOrderHistory(@RequestBody OrderHistoryReq orderHistoryReq,
                                                                 @RequestHeader String access,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(orderService.getOrderHistory(orderHistoryReq));
    }

    @GetMapping("/history/{detailHistoryId}")
    @Operation(summary = "상세주문내역", description = "<b>long : detailHistoryId")
    public ResponseEntity<List<DetailHistoryRes>> getDetailHistory(@PathVariable long detailHistoryId,
                                                                   @RequestHeader String access,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getDetailHistory(detailHistoryId));
    }

    @PostMapping("/refund")
    @Operation(summary = "환불 요청", description = "<b>long : orderHistoryId" +
            "<br>orderHistoryId에 환불요청")
    public ResponseEntity<Long> makeRefundRequest(@RequestBody RefundRequestCreateReq req,
                                                  @RequestHeader String access,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.makeRefundRequest(req));
    }
}
