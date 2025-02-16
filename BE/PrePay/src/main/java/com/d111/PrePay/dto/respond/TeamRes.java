package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
@Setter
public class TeamRes {
    private String teamName;
    private boolean publicTeam;
    private int balance;
    private int teamBalance;
    private Long teamId;
    private String color;
    private long genDate;

    public TeamRes(UserTeam userTeam, int teamBalance) {
        this.teamId = userTeam.getTeam().getId();
        this.teamName = userTeam.getTeam().getTeamName();
        this.publicTeam = userTeam.getTeam().isPublicTeam();
        this.balance = userTeam.getTeam().getDailyPriceLimit() - userTeam.getUsedAmount();
        this.teamBalance = teamBalance;
        this.color=userTeam.getTeam().getColor();
        this.genDate = userTeam.getTeam().getGenDate();
        log.info("dailyLimit : {} , usedAmount : {}", userTeam.getTeam().getDailyPriceLimit(), userTeam.getUsedAmount());
    }
}
