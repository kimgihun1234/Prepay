package com.d111.PrePay.dto.respond;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteCodeRes {
    private String inviteCode;
    private long initTime;

    public InviteCodeRes(String password, long l) {
        this.inviteCode=password;
        this.initTime=l;
    }
}
