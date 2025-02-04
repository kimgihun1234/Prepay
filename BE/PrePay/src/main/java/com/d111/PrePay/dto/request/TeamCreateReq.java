package com.d111.PrePay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TeamCreateReq {
    private boolean publicTeam;
    private String teamName;
    private int dailyPriceLimit;
    private int countLimit;
    private String teamMessage;

}
