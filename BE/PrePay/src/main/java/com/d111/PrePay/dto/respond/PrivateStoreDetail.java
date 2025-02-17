package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.TeamStore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateStoreDetail {
    String storeName;
    String storeImgUrl;
    String storeDescription;
    int balance;
    float longitude;
    float latitude;

    public PrivateStoreDetail(TeamStore teamStore, Store store) {
        this.storeName = store.getStoreName();
        this.storeImgUrl = store.getStoreImgUrl();
        this.storeDescription = store.getStoreDescription();
        this.longitude = store.getLongitude();
        this.latitude = store.getLatitude();
        this.balance=teamStore.getTeamStoreBalance();
    }
}
