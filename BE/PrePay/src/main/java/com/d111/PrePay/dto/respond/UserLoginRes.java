package com.d111.PrePay.dto.respond;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRes {
    private String userName;
    private String jwtToken;
}
