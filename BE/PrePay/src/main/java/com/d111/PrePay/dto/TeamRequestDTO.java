package com.d111.PrePay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TeamRequestDTO {
    private Long userId;
    private boolean publicTeam;
    private String teamName;
    private int dailyPriceLimit;
    private int countLimit;
    private String teamMessage;

}
