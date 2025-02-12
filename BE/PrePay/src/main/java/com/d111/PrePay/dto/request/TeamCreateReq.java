package com.d111.PrePay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TeamCreateReq {
    private String teamName;
    private boolean publicTeam;
    private int dailyPriceLimit;
    private int countLimit;
    private String teamMessage;
    private String color;
}
