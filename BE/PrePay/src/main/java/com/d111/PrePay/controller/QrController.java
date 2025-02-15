package com.d111.PrePay.controller;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.security.dto.CustomUserDetails;
import com.d111.PrePay.service.QrService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    @GetMapping("/{teamId}/party")
    @Operation(summary = "회식용 qr 생성")
    public ResponseEntity<StandardRes> getPartyQr(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
        String email = userDetails.getUsername();
        StandardRes result = qrService.getPartyQr(email, teamId);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/{teamId}/private")
    @Operation(summary = "개인용 qr 생성")
    public ResponseEntity<StandardRes> getPrivateQr(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable long teamId){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(qrService.getPrivateQr(email,teamId));
    }

}
