package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicTeamLikedRes {
    private Long teamId;
    private String imageUrl;
    private int dailyLimit;
    private int usedAmount;
    private String teamMessage;
    private boolean checkLike;
    private String teamName;
    private String address;
    private int teamBalance;
    private float distance;

    public PublicTeamLikedRes(UserTeam userTeam, Team team) {
        this.teamId = team.getId();
        this.imageUrl=team.getTeamImgUrl();
        this.dailyLimit=team.getDailyPriceLimit();
        this.usedAmount=userTeam.getUsedAmount();
        this.teamMessage = team.getTeamMessage();
        this.checkLike= userTeam.isLike();;
        this.teamName = team.getTeamName();
        this.teamBalance = team.getTeamStores().stream().mapToInt(teamStore->teamStore.getTeamStoreBalance()
        ).sum();
        this.address = team.getTeamStores().get(0).getStore().getAddress();
    }
}
