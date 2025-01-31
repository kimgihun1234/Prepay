package com.d111.PrePay.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamDetailResponseDTO {
    private Long teamId;
    private String teamName;
    private int dailyPriceLimit;
    private boolean publicTeam;
    private int countLimit;
    private boolean position;
    private String teamMessage;
}
