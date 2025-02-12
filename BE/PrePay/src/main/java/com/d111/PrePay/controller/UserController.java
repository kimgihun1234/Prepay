package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.FcmTokenReq;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.service.UserService;
import com.d111.PrePay.dto.request.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<UserSignUpRes> signup(@RequestBody UserSignUpReq userSignUpReq) {
        UserSignUpRes userSignUpRes = userService.userSignUp(userSignUpReq);
        return ResponseEntity.ok(userSignUpRes);
    }

    @PostMapping("/setFcmToken")
    @Operation(summary = "fcm 토큰 설정"
    )
    public ResponseEntity<StandardRes>setFcmToken(@RequestBody FcmTokenReq req, @RequestHeader String email ){
        return ResponseEntity.ok(userService.setFcmToken(req, email));
    }

}
