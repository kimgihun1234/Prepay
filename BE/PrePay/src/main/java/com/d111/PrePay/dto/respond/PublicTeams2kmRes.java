package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicTeams2kmRes {
    private long teamId;
    private String teamName;
    private String teamMessage;
    private int teamBalance;
    private String teamInitializerNickname;
    private boolean isLike;
    private String imgUrl;
    private float latitude;
    private float longitude;
    private double distance;

    public PublicTeams2kmRes(Team team) {
        this.teamName = team.getTeamName();
        this.teamId = team.getId();
        this.teamMessage = team.getTeamMessage();
        this.teamBalance = team.getTeamBalance();
        this.imgUrl=team.getTeamImgUrl();
    }
}
