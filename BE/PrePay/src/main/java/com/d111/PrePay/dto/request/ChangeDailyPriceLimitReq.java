package com.d111.PrePay.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeDailyPriceLimitReq {
    private Long teamId;
    private int dailyPriceLimit;

}
