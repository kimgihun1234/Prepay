package com.d111.PrePay.dto.respond;


import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.UserTeam;
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
    private String teamPassword;

    public TeamDetailRes(Team team, UserTeam userTeam){
        this.teamId = team.getId();
        this.teamName = team.getTeamName();
        this.dailyPriceLimit = team.getDailyPriceLimit();
        this.publicTeam = team.isPublicTeam();
        this.teamPassword = team.getTeamPassword();
        this.countLimit = team.getCountLimit();
        this.position =  userTeam.isPosition();
        this.teamMessage = team.getTeamMessage();
    }
}
