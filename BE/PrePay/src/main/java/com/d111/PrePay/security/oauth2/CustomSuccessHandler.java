package com.d111.PrePay.security.oauth2;

import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.security.dto.CustomOauth2User;
import com.d111.PrePay.security.jwt.JWTUtil;
import com.d111.PrePay.service.FCMService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JWTUtil jwtUtil;
    private final FCMService fcmService;
    private final UserRepository userRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil,FCMService fcmService,UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.fcmService = fcmService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOauth2User customUserDetails= (CustomOauth2User) authentication.getPrincipal();
        
        String name = customUserDetails.getName();
        Long userId = customUserDetails.getUserId();
        User findUser = userRepository.findById(userId).orElseThrow();
        String fcmToken = findUser.getFcmToken();
        String token = jwtUtil.createJWT("access",name,userId, 60*60*60L);

//        fcmService.sendDataMessageTo(fcmToken,token);
        response.sendRedirect("http://localhost:3000/");

//        response.addCookie(createCookie("access", token));
//        response.sendRedirect("http://localhost:3000/");

    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");

        return cookie;
    }

}
