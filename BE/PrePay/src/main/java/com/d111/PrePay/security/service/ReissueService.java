package com.d111.PrePay.security.service;


import com.d111.PrePay.security.entity.Refresh;
import com.d111.PrePay.security.jwt.JWTUtil;
import com.d111.PrePay.security.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }


    public ResponseEntity<?> createRefresh(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        refresh = request.getHeader("refresh");


        if (refresh == null) {

            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 리프레쉬 토큰 만료 재 로그인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }



        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        Long userId = jwtUtil.getUserId(refresh);

        String newAccess = jwtUtil.createJWT("access", email, 600000 * 6L, userId);
        String newRefresh = jwtUtil.createJWT("refresh", email, 600000 * 12L, userId);

        log.info("새로운 액세스 토큰 : {}",newAccess);
        log.info("새로운 리프레쉬 토큰 : {}",newRefresh);

        refreshRepository.deleteByRefresh(refresh);
        addRefresh(email, newRefresh, 86400000L);
        response.setHeader("access", newAccess);
        response.setHeader("refresh",newRefresh);

        return new ResponseEntity<>(HttpStatus.OK);

    }


    private void addRefresh(String email, String refreshTokenName, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh = new Refresh();
        refresh.setEmail(email);
        refresh.setRefresh(refreshTokenName);
        refresh.setExpiration(date.toString());

        refreshRepository.save(refresh);
    }

//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setAttribute("SameSite", "None");  // 크로스 사이트 요청에서도 쿠키 허용
//        cookie.setSecure(true);
//
//        return cookie;
//    }

}
