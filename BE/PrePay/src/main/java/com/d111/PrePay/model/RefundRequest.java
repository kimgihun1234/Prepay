package com.d111.PrePay.model;

import com.d111.PrePay.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RefundRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_request_id")
    public long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private int requestPrice;

    private long requestDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Store store;

    @OneToOne
    @JoinColumn(name="order_history_id")
    private OrderHistory orderHistory;



}
