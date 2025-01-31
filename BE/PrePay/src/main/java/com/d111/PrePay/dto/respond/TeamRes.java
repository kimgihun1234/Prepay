package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class TeamRes {
    private String teamName;
    private boolean publicTeam;
    private int balance;

    public TeamRes(UserTeam userTeam) {
        this.teamName = userTeam.getTeam().getTeamName();
        this.publicTeam = userTeam.getTeam().isPublicTeam();
        this.balance = userTeam.getTeam().getDailyPriceLimit() - userTeam.getUsedAmount();
        log.info("dailyLimit : {} , usedAmount : {}", userTeam.getTeam().getDailyPriceLimit(), userTeam.getUsedAmount());
    }
}
