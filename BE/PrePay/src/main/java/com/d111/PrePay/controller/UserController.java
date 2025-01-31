package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.UserCreateReq;
import com.d111.PrePay.dto.request.UserLoginReq;
import com.d111.PrePay.dto.respond.UserLoginRes;
import com.d111.PrePay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Long> makeUser(@RequestBody UserCreateReq req) {
        return ResponseEntity.ok(userService.makeUser(req));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> login(@RequestBody UserLoginReq request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
