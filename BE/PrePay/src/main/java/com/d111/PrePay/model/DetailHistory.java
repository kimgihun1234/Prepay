package com.d111.PrePay.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DetailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_history_id")
    private Long id;

    private String product;

    private int detailPrice;

    private int quantity;

    @ManyToOne
    private OrderHistory orderHistory;

}
