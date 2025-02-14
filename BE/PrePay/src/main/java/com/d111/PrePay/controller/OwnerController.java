package com.d111.PrePay.controller;


import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.respond.DetailHistoryRes;
import com.d111.PrePay.dto.respond.GetPrepaidTeamsRes;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.service.OrderService;
import com.d111.PrePay.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;
    private final OrderService orderService;

    @PostMapping("/teams")
    @Operation(summary = "가게에 선결제 한 팀 리스트 조회")
    public ResponseEntity<List<GetPrepaidTeamsRes>> getPrepaidTeams(@RequestHeader Long storeId) {
        return ResponseEntity.ok(ownerService.getPrepaidTeams(storeId));
    }


    @PostMapping("/history")
    @Operation(summary = "사장님 주문내역", description = "<b>long : teamId<br>" +
            "long : storeId")
    public ResponseEntity<List<OrderHistoryRes>> getOrderHistory(@RequestBody OrderHistoryReq orderHistoryReq) {

        return ResponseEntity.ok(ownerService.getOrderHistory(orderHistoryReq));
    }

    @GetMapping("/history/{detailHistoryId}")
    @Operation(summary = "사장님 상세주문내역", description = "<b>long : detailHistoryId")
    public ResponseEntity<List<DetailHistoryRes>> getDetailHistory(@PathVariable long detailHistoryId) {
        return ResponseEntity.ok(ownerService.getDetailHistory(detailHistoryId));
    }


}
