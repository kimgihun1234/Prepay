package com.d111.PrePay.controller;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.service.QrService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    @GetMapping("/{teamId}/party")
    @Operation(summary = "회식용 qr 생성")
    public ResponseEntity<StandardRes> getPartyQr(@RequestHeader String userEmail, @PathVariable Long teamId) {
        StandardRes result = qrService.getPartyQr(userEmail, teamId);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @GetMapping("/private")
    @Operation(summary = "개인용 qr 생성")
    public ResponseEntity<StandardRes> getPrivateQr(@RequestHeader String userEmail){

        return ResponseEntity.ok(qrService.getPrivateQr(userEmail));
    }

}
