package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailHistoryReq {
    private String product;
    private int detailPrice;
    private int quantity;
}
