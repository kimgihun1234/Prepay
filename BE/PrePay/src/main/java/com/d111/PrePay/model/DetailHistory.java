package com.d111.PrePay.model;

import com.d111.PrePay.dto.request.DetailHistoryReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DetailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_history_id")
    private Long id;

    private String product;

    private int detailPrice;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderHistory orderHistory;

    public DetailHistory(DetailHistoryReq detailHistoryReq) {
        this.product = detailHistoryReq.getProduct();
        this.detailPrice = detailHistoryReq.getDetailPrice();
        this.quantity = detailHistoryReq.getQuantity();
    }
}
