package com.d111.PrePay.controller;


import com.d111.PrePay.dto.request.StoresReq;
import com.d111.PrePay.dto.respond.AllStoreRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.security.dto.CustomUserDetails;
import com.d111.PrePay.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/stores/{teamId}")
    @Operation(summary = "팀 가맹점 추가용 가맹점 리스트 조회")
    public ResponseEntity<List<AllStoreRes>> getNewStoresForPrivate(@RequestParam long teamId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(storeService.getNewStoresForPrivate(teamId,email));
    }

    @GetMapping("/stores/all")
    @Operation(summary = "전체 가게 조회")
    public ResponseEntity<List<AllStoreRes>> getAllStores(){
        return ResponseEntity.ok(storeService.getAllStores());
    }


}
