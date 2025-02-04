package com.d111.PrePay.dto.request;


import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Transactional
public class SignInTeamReq {
    private String teamPassword;

}
