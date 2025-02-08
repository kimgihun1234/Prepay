package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateReq {
    private long userTeamId;
    private long storeId;
    private boolean companyDinner;
    private String qrUUID;
    private List<DetailHistoryReq> details;
}
