package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateReq {
    private String userName;
    private String userLoginId;
    private String password;
    private String email;
    private String nickname;
}
