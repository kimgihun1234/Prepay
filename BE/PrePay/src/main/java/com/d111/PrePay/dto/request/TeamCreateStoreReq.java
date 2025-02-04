package com.d111.PrePay.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateStoreReq {
    private Long storeId;
    private Long teamId;
    private int balance;

}
