package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.FcmTokenReq;
import com.d111.PrePay.dto.request.UserSignUpReq;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.dto.respond.UserSignUpRes;
import com.d111.PrePay.exception.DuplicateUserException;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserSignUpRes userSignUp(UserSignUpReq userSignUpReq) {
        User user = new User(userSignUpReq);
        List<User> all = userRepository.findAll();
        for (User findUser : all) {
            if (user.getEmail().equals(findUser.getEmail())){
                throw new DuplicateUserException("이미 사용중인 이메일입니다.");
            }
            if (user.getNickname().equals(findUser.getNickname())){
                throw  new DuplicateUserException("이미 사용중인 닉네임입니다.");
            }
        }

        user.setUserPassword(bCryptPasswordEncoder.encode(userSignUpReq.getPassword()));
        user.setEmail(userSignUpReq.getEmail());
        userRepository.save(user);
        UserSignUpRes userSignUpRes = UserSignUpRes.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .build();
        return userSignUpRes;
    }

    @Transactional
    public StandardRes setFcmToken(FcmTokenReq req, String email) {
        User user = userRepository.findUserByEmail(email);
        user.setFcmToken(req.getToken());
        return new StandardRes("토큰 저장 완료",200);
    }
}
