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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserSignUpRes userSignUp(UserSignUpReq userSignUpReq) {
        User user = new User(userSignUpReq);
        user.setUserPassword(bCryptPasswordEncoder.encode(userSignUpReq.getPassword()));
        user.setEmail(userSignUpReq.getEmail());
        userRepository.save(user);
        UserSignUpRes userSignUpRes = UserSignUpRes.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .build();
        return userSignUpRes;
    }
}
