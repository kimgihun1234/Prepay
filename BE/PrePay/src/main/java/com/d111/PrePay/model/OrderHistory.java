package com.d111.PrePay.model;


import com.d111.PrePay.RequestStatus;
import com.d111.PrePay.dto.request.DetailHistoryReq;
import com.d111.PrePay.dto.request.OrderReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private long id;

    private long orderDate;

    private int totalPrice;

    private boolean refundRequested;

    private boolean companyDinner;

    // 환불일 때 금액 추가임을 설정하는 t/f 추후 상속(소비, 환불)으로 분리해야함
    private boolean withDraw;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Store store;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "orderHistory")
    private RefundRequest refundRequest;

    @OneToMany(mappedBy = "orderHistory")
    private List<DetailHistory> detailHistories;

    public OrderHistory(OrderReq orderReq) {
        this.orderDate = System.currentTimeMillis();
        this.companyDinner = orderReq.isCompanyDinner();
        this.withDraw = true;
        this.totalPrice = 0;
        this.refundRequested = false;
        for (DetailHistoryReq detailHistoryReq : orderReq.getDetails()) {
            this.totalPrice += detailHistoryReq.getDetailPrice() * detailHistoryReq.getQuantity();
        }
    }
}
