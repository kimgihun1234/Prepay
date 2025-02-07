package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.FcmTokenReq;
import com.d111.PrePay.dto.request.UserLoginReq;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.dto.respond.UserLoginRes;
import com.d111.PrePay.service.FCMService;
import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.service.UserService;
import com.d111.PrePay.dto.request.UserSignUpReq;
import com.d111.PrePay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpRes> signup(@RequestBody UserSignUpReq userSignUpReq) {
        UserSignUpRes userSignUpRes = userService.userSignUp(userSignUpReq);
        return ResponseEntity.ok(userSignUpRes);
    }

}
