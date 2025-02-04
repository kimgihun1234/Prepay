package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.TeamStore;
import lombok.Getter;

@Getter
public class StoresRes {
    private long storeId;
    private String storeName;
    private int balance;

    public StoresRes(TeamStore teamStore) {
        this.storeId=teamStore.getStore().getId();
        this.storeName=teamStore.getStore().getStoreName();
        this.balance= teamStore.getTeamStoreBalance();
    }
}
