package com.d111.PrePay.dto.respond;

import lombok.Getter;

@Getter
public class TokenRes {
    private String access;
    private String refresh;

    public TokenRes(String access,String refresh) {
        this.access = access;
        this.refresh = refresh;
    }
}


