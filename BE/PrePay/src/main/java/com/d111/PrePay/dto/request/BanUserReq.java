package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BanUserReq {
    private String banUserEmail;
    private Long teamId;

}
