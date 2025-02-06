package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.FcmTokenReq;
import com.d111.PrePay.dto.request.LoginReq;
import com.d111.PrePay.dto.request.UserLoginReq;
import com.d111.PrePay.dto.request.UserSignUpReq;
import com.d111.PrePay.dto.respond.LoginRes;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.dto.respond.UserLoginRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserSignUpRes userSignUp(UserSignUpReq userSignUpReq) {
        User user = new User(userSignUpReq);
        userRepository.save(user);
        UserSignUpRes userSignUpRes = UserSignUpRes.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .build();
        return userSignUpRes;
    }


    public UserLoginRes login(UserLoginReq req) {
        User user = userRepository.findByEmailAndUserPasswordEquals(req.getEmail(), req.getPassword());
        if (user != null) {
            UserLoginRes res = new UserLoginRes();
            res.setJwtToken(String.valueOf(user.getId()));
            res.setUserName(user.getUserName());
            return res;
        } else {
            throw new RuntimeException(); // 로그인 실패 예외처리
        }
    }

    public StandardRes updateToken(FcmTokenReq fcmTokenReq) {
        User user = userRepository.findUserByEmail(fcmTokenReq.getEmail());
        user.setFcmToken(fcmTokenReq.getToken());
        log.info("유저명 : {}, 토큰 : {}",user.getUserName(),user.getFcmToken());
        return new StandardRes("토큰 업데이트 완료",200);
    }
}
