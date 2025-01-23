package com.d111.PrePay.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private long id;

    private String storeName;

    private String address;

    private String type;

    private boolean userRegisterPermission;

    private float longitude;

    private float latitude;

    @OneToMany(mappedBy = "store")
    private List<TeamStore> teamStores = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<RefundRequest> refundRequests = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<OrderHistory> orderHistories = new ArrayList<>();
}
