package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.UserCreateReq;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long makeUser(UserCreateReq req) {

        User user = new User(req);

        userRepository.save(user);

        return user.getId();

    }
}
