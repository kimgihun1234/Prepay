package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateReq {
    private String email;
    private long teamId;
    private long storeId;
    private String qrUUID;
    private List<DetailHistoryReq> details;
}
