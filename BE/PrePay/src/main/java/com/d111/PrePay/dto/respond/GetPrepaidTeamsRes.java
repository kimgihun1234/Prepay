package com.d111.PrePay.dto.respond;


import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPrepaidTeamsRes {
    private String teamName;
    private int storeBalance;
    private String imgUrl;
    private boolean publicTeam;
    private String email;
    private String nickname;
    private String teamMessage;
    private int dailyPriceLimit;

    public GetPrepaidTeamsRes(TeamStore teamStore, Team team) {
        User teamInitializer = team.getTeamInitializer();
        this.teamName = team.getTeamName();
        this.storeBalance = teamStore.getTeamStoreBalance();
        this.imgUrl = team.getTeamImgUrl();
        this.publicTeam = team.isPublicTeam();
        this.teamMessage = team.getTeamMessage();
        this.dailyPriceLimit = team.getDailyPriceLimit();
        this.email = teamInitializer.getEmail();
        this.nickname = teamInitializer.getNickname();
    }
}
