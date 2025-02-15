package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.TeamStore;
import lombok.Getter;

@Getter
public class StoresCorRes {
    private float longitude;
    private float latitude;
    private String storeName;

    public StoresCorRes(Store store) {
        this.longitude = store.getLongitude();
        this.latitude = store.getLatitude();
        this.storeName = store.getStoreName();
    }
}
