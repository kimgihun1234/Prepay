package com.d111.PrePay.controller;


import com.d111.PrePay.dto.respond.GetPrepaidTeamsRes;
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

    @PostMapping("/teams")
    @Operation(summary = "가게에 선결제 한 팀 리스트 조회")
    public ResponseEntity<List<GetPrepaidTeamsRes>> getPrepaidTeams(@RequestHeader Long storeId){
        return ResponseEntity.ok(ownerService.getPrepaidTeams(storeId));
    }




}
