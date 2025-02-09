package com.d111.PrePay.security.oauth2;

import com.d111.PrePay.security.dto.CustomOauth2User;
import com.d111.PrePay.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOauth2User customUserDetails= (CustomOauth2User) authentication.getPrincipal();
        
        String name = customUserDetails.getName();
        Long userId = customUserDetails.getUserId();
        String token = jwtUtil.createJWT("access",name,userId, 60*60*60L);

        response.addCookie(createCookie("access", token));
        response.sendRedirect("http://localhost:3000/");

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
