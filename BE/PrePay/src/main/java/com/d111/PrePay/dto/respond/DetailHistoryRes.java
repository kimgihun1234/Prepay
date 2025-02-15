package com.d111.PrePay.dto.respond;


import com.d111.PrePay.model.DetailHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailHistoryRes {
    private long detailHistoryId;
    private String product;
    private int detailPrice;
    private int quantity;

    public DetailHistoryRes(DetailHistory detailHistory) {
        this.detailHistoryId = detailHistory.getId();
        this.product = detailHistory.getProduct();
        this.detailPrice = detailHistory.getDetailPrice();
        this.quantity = detailHistory.getQuantity();
    }
}
