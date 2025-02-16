package com.d111.PrePay.dto.respond;

import com.d111.PrePay.model.Store;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AllStoreRes {
    private Long storeId;
    private String storeName;
    private String address;
    private String type;
    private String storeImgUrl;
    private String storeDescription;

    public AllStoreRes(Store store) {
        this.storeId = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.type = store.getType();
        this.storeImgUrl = store.getStoreImgUrl();
        this.storeDescription = store.getStoreDescription();
    }
}
