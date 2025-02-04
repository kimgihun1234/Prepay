package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BanUserReq {
    private Long banUserId;
    private Long teamId;

}
