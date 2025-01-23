package com.d111.PrePay.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter @Setter
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private long id;

    private long orderDate;

    private int totalPrice;

    private int refundStatus;

    private boolean companyDinner;

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
}
