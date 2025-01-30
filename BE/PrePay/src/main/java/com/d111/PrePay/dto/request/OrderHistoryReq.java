package com.d111.PrePay.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderHistoryReq {
    private Long teamId;
    private Long storeId;
}
