package com.d111.PrePay.dto.respond;


import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class TeamCreateStoreRes {
    private Long teamStoreId;
    private Long teamId;
    private Long storeId;
    private int teamStoreBalance;
    private String storeImgUrl;


}
