package com.d111.PrePay.dto.request;

import lombok.Getter;

@Getter
public class UserSignUpReq {
    private String userName;
    private String password;
    private String email;
    private String nickname;
}
