package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.dto.respond.UserLoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String userSignUp(UserSignUpReq userSignUpReq) {
        User user = new User(userSignUpReq);
        userRepository.save(user);
        return "유저 " + user.getUserName() + "생성완료";
    }


    public UserLoginRes login(UserLoginReq req) {
        User user = userRepository.findUserByUserLoginIdAndUserPassword(req.getUserLoginId(), req.getPassword());
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
        return new StandardRes("토큰 업데이트 완료","201");
    }
}
