package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.LoginReq;
import com.d111.PrePay.dto.request.UserSignUpReq;
import com.d111.PrePay.dto.respond.LoginRes;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.dto.request.UserCreateReq;
import com.d111.PrePay.dto.request.UserLoginReq;
import com.d111.PrePay.dto.respond.UserLoginRes;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findByEmailAndUserPassword(req.getUserLoginId(), req.getPassword());
        if (user != null) {
            UserLoginRes res = new UserLoginRes();
            res.setJwtToken(String.valueOf(user.getId()));
            res.setUserName(user.getUserName());
            return res;
        } else {
            throw new RuntimeException(); // 로그인 실패 예외처리
        }
    }
}
