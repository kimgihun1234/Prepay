package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.FcmTokenReq;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.dto.respond.TokenRes;
import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.service.KaKaoService;
import com.d111.PrePay.service.UserService;
import com.d111.PrePay.dto.request.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final KaKaoService kakaoService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<UserSignUpRes> signup(@RequestBody UserSignUpReq userSignUpReq) {
        UserSignUpRes userSignUpRes = userService.userSignUp(userSignUpReq);
        return ResponseEntity.ok(userSignUpRes);
    }

    @PostMapping("/kakao/login/{code}")
    @Operation(summary = "카카오 로그인")
    public ResponseEntity<?> kakaoLogin(@PathVariable String code, HttpServletRequest request, HttpServletResponse response) {

        String accessToken = kakaoService.getAccessToken(code);
        HashMap<String, Object> kaKaoUserInfo = kakaoService.getKaKaoUserInfo(accessToken);
        TokenRes tokenRes = kakaoService.kakaoUserLogin(kaKaoUserInfo);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "로그인 성공");

        ResponseCookie refreshCookie = ResponseCookie.from("refresh", tokenRes.getRefresh())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header("access", tokenRes.getAccess())
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }
    @PostMapping("/setFcmToken")
    @Operation(summary = "fcm 토큰 설정"
    )
    public ResponseEntity<StandardRes>setFcmToken(@RequestBody FcmTokenReq req, @RequestHeader String email ){
        return ResponseEntity.ok(userService.setFcmToken(req, email));
    }

}

