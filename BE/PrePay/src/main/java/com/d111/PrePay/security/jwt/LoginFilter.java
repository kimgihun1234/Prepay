package com.d111.PrePay.security.jwt;

import com.d111.PrePay.dto.request.LoginReq;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.security.entity.Refresh;
import com.d111.PrePay.security.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private UserRepository userRepository;
    private RefreshRepository refreshRepository;


    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
    }

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepository userRepository,
                       RefreshRepository refreshRepository) {
        setFilterProcessesUrl("/user/login");
        setUsernameParameter("email");
        setPasswordParameter("userPassword");
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        LoginReq loginReq = new LoginReq();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginReq = objectMapper.readValue(messageBody, LoginReq.class);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        String email = loginReq.getEmail();
        String password = loginReq.getPassword();


        System.out.println(email);
        System.out.println(password);

        UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authtoken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email);
        Long userId = user.getId();


        String access = jwtUtil.createJWT("access", email, 600000L, userId);
        String refresh = jwtUtil.createJWT("refresh", email, 86400000L, userId);

        addRefresh(email, refresh, 86400000L);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

    }

    private void addRefresh(String email, String refreshTokenName, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh = new Refresh();
        refresh.setEmail(email);
        refresh.setRefresh(refreshTokenName);
        refresh.setExpiration(date.toString());

        refreshRepository.save(refresh);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("3123");
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");  // 크로스 사이트 요청에서도 쿠키 허용
        cookie.setSecure(true);

        return cookie;
    }


}
