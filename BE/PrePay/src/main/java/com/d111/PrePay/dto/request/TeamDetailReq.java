package com.d111.PrePay.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamDetailReq {
    private Long userId;
    private Long teamId;
}
