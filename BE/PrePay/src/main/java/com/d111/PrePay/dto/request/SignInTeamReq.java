package com.d111.PrePay.dto.request;


import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInTeamReq {
    private String teamPassword;

}
