package com.d111.PrePay.controller;

import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.service.UserService;
import com.d111.PrePay.dto.request.UserSignUpReq;
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
    public ResponseEntity<UserSignUpRes> signup(@RequestBody UserSignUpReq userSignUpReq) {
        UserSignUpRes userSignUpRes = userService.userSignUp(userSignUpReq);
        return ResponseEntity.ok(userSignUpRes);
    }

}
