package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicTeamDetailRes {
    private String imageUrl;
    private int dailyLimit;
    private int usedAmount;
    private String teamMessage;
    private boolean checkLike;
    private String teamName;
    private String address;
    private int teamBalance;
    private float latitude;
    private float longitude;
    private String storeUrl;
    private String storeName;
    private String storeDescription;

    public PublicTeamDetailRes(UserTeam userTeam, Team team) {
        this.imageUrl = team.getTeamImgUrl();
        this.dailyLimit = team.getDailyPriceLimit();
        this.usedAmount = userTeam.getUsedAmount();
        this.teamMessage = team.getTeamMessage();
        this.checkLike = userTeam.isLike();
        this.teamName = team.getTeamName();
        this.teamBalance = team.getTeamStores().stream().mapToInt(teamStore -> teamStore.getTeamStoreBalance()
        ).sum();
        Store store = team.getTeamStores().get(0).getStore();
        this.address = store.getAddress();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.storeUrl = store.getStoreImgUrl();
        this.storeName = store.getStoreName();
        this.storeDescription = store.getStoreDescription();
    }
}
