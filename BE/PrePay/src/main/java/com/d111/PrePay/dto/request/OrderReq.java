package com.d111.PrePay.dto.request;

import com.d111.PrePay.model.DetailHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderReq {
    private long userTeamId;
    private long storeId;
    private boolean companyDinner;
    private List<DetailHistoryReq> details;
}
