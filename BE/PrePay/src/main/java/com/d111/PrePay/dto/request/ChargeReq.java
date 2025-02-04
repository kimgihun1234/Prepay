package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class ChargeReq {
    private Long teamId;
    private Long storeId;
    private int requestPrice;
}
