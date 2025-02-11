package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.TeamStore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoresRes {
    private long storeId;
    private String storeName;
    private int balance;
    private float latitude;
    private float longitude;
    private boolean isMyteam;
    private boolean isLike;

    public StoresRes(TeamStore teamStore) {
        this.storeId=teamStore.getStore().getId();
        this.storeName=teamStore.getStore().getStoreName();
        this.balance= teamStore.getTeamStoreBalance();
    }

    public StoresRes(Store store) {
        this.storeId = store.getId();
        this.storeName=store.getStoreName();

    }
}
