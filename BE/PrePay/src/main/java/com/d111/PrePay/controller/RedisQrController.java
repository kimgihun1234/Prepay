package com.d111.PrePay.controller;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.service.RedisQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("redis/")
@RequiredArgsConstructor
public class RedisQrController {

    private final RedisQrService redisQrService;

    @GetMapping("/{teamId}/private")
    public ResponseEntity<StandardRes> getRedisQr(@RequestHeader String email, @PathVariable long teamId) {
        return ResponseEntity.ok(redisQrService.getRedisQr(email, teamId));
    }
}
