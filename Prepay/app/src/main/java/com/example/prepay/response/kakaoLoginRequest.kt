package com.example.prepay.response

data class KakaoLoginRequest(
        val accessToken: String
)

data class KakaoLoginResponse(
        val message: String,
        // 헤더는 key 값에 여러 값이 올 수 있으므로 Map<String, List<String>> 타입으로 선언
        val headers: Map<String, List<String>>? = null,
        // 쿠키는 여러 개일 수 있으므로 List<String> 타입으로 선언 (예: "Set-Cookie" 값)
        val cookies: List<String>? = null
)