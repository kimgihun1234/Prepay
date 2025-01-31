package com.d111.PrePay.model;

import com.d111.PrePay.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_request_id")
    public Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private int requestPrice;

    private long requestDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Store store;

    @OneToOne
    @JoinColumn(name = "order_history_id")
    private OrderHistory orderHistory;


    public RefundRequest(OrderHistory orderHistory) {
        this.requestStatus = RequestStatus.Waiting;
        this.requestPrice = orderHistory.getTotalPrice();
        this.requestDate = System.currentTimeMillis();
        this.user = orderHistory.getUser();
        this.store = orderHistory.getStore();
        this.orderHistory = orderHistory;
    }
}
