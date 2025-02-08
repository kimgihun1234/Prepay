package com.d111.PrePay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateStoreReq {
    private String storeName;
    private String type;
    private boolean userRegisterPermission;
    private String address;
    private float latitude;
    private float longitude;
}
