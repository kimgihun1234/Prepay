package com.d111.PrePay.dto.respond;

import com.d111.PrePay.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderHistoryRes {
    private long orderHistoryId;
    private long orderDate;
    private int totalPrice;
    private boolean isWithdraw;
    private RequestStatus requestStatus;
    private boolean isCompanyDinner;
}
