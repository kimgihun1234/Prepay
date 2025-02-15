package com.d111.PrePay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignUpReq {
    private String email;
    private String password;
    private String nickname;
}
