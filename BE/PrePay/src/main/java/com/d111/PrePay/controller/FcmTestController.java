package com.d111.PrePay.controller;

import com.d111.PrePay.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FcmTestController {

    private final FCMService fcmService;

//    @PostMapping("/sendDataMessageTo")
//    public void sendDataMessageTo(String token, String title, String body) throws IOException {
//        log.info("sendMessageTo : token:{}, title:{}, body:{}", token, title, body);
//        fcmService.sendDataMessageTo(token, title, body);
//    }
}
