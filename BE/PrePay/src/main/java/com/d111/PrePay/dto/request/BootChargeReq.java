package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BootChargeReq {
    private long teamId;
    private long storeId;
    private int amount;
    private String receiptId;
}
