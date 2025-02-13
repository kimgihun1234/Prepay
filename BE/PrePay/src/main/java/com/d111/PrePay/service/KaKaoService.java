package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.KaKaoUserInfo;
import com.d111.PrePay.dto.respond.TokenRes;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.security.entity.Refresh;
import com.d111.PrePay.security.jwt.JWTUtil;
import com.d111.PrePay.security.repository.RefreshRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KaKaoService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Value("${kakao.client_id}")
    private String clientId;

//    @Value("${kakao.redirect_uri}")
//    private String redirectUri;
    String tokenUrl = "https://kauth.kakao.com/oauth/token";

    public String getAccessToken(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
//        params.add("redirect_uri", redirectUri); // 카카오에 등록된 리다이렉트 URI
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

        String body = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String accessToken = jsonNode.get("access_token").asText();
        return accessToken;
    }

    public HashMap<String, Object> getKaKaoUserInfo(String accessToken){
        HashMap<String, Object> userInfo = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                Object.class
        );


        LinkedHashMap<?, ?> responseBody = (LinkedHashMap<?, ?>) response.getBody();
        log.info("response : {}",response);
        log.info("responseBody : {}",responseBody);
        Long id = ((Number) responseBody.get("id")).longValue();
        String nickname = (String) ((Map<String, Object>) responseBody.get("properties")).get("nickname");
        String email = (String) ((Map<String, Object>) responseBody.get("kakao_account")).get("email");
//        ObjectMapper objectMapper = new ObjectMapper();
//        Long id = kaKaoUserInfo.getId();
//        String email = kaKaoUserInfo.getKakao_account().get("email");
//        String nickname = kaKaoUserInfo.getProperties().getNickname();

        log.info("카카오 PK : {}",id);
        log.info("이메일 : {}",email);
        log.info("닉네임 : {}",nickname);

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }

    public TokenRes kakaoUserLogin(HashMap<String,Object> userInfo) {
        Long kid = Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        User kakaoUser = userRepository.findByKakaoId(kid);
        String access = null;
        String refresh = null;

        if (kakaoUser == null) {
            User user = new User();
            user.setNickname(nickName);
            user.setKakaoId(kid);
            user.setEmail(kakaoEmail);
            User savedUser = userRepository.save(user);

            access = jwtUtil.createJWT("access", kakaoEmail, 60000L, savedUser.getId());
            refresh = jwtUtil.createJWT("refresh", kakaoEmail, 120000L, savedUser.getId());

        } else {
            access = jwtUtil.createJWT("access", kakaoEmail, 60000L, kakaoUser.getId());
            refresh = jwtUtil.createJWT("refresh", kakaoEmail, 120000L, kakaoUser.getId());

        }
        addRefresh(refresh, 86400000L);

        TokenRes tokenRes = new TokenRes(access, refresh);
        return tokenRes;

    }  private void addRefresh(String refreshTokenName, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh = new Refresh();
        refresh.setRefresh(refreshTokenName);
        refresh.setExpiration(date.toString());

        refreshRepository.save(refresh);
    }


    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");  // 크로스 사이트 요청에서도 쿠키 허용
        cookie.setSecure(true);

        return cookie;
    }


}
