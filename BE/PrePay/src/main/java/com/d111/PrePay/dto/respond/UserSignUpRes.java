package com.d111.PrePay.dto.respond;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpRes {
    private boolean success;
    private String message;

}
