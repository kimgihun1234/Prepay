package com.d111.PrePay.dto.respond;

import com.d111.PrePay.RequestStatus;
import com.d111.PrePay.model.OrderHistory;
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
    private boolean refundRequested;
    private boolean isCompanyDinner;

    public OrderHistoryRes(OrderHistory orderHistory) {
        this.orderHistoryId = orderHistory.getId();
        this.orderDate = orderHistory.getOrderDate();
        this.totalPrice = orderHistory.getTotalPrice();
        this.isWithdraw = orderHistory.isWithDraw();
        this.refundRequested = orderHistory.isRefundRequested();
        this.isCompanyDinner = orderHistory.isCompanyDinner();
    }
}
