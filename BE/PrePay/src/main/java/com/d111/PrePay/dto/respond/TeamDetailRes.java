package com.d111.PrePay.dto.respond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TeamDetailRes {
    private Long teamId;
    private String teamName;
    private int dailyPriceLimit;
    private boolean publicTeam;
    private int countLimit;
    private boolean position;
    private String teamMessage;
}
