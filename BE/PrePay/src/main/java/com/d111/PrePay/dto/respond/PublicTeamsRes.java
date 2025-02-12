package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicTeamsRes {
    private long teamId;
    private String teamName;
    private String teamMessage;
    private int teamBalance;
    private String teamInitializerNickname;
    private boolean isLike;

    public PublicTeamsRes(Team team) {
        this.teamName = team.getTeamName();
        this.teamId = team.getId();
        this.teamMessage = team.getTeamMessage();
        this.teamBalance = team.getTeamBalance();
    }
}
