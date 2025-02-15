package com.d111.PrePay.model;

import com.d111.PrePay.dto.request.CreateStoreReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    private String storeName;

    private String address;

    private String type;

    private boolean userRegisterPermission;

    private float longitude;

    private float latitude;

    private String storeImgUrl;

    private String storeDescription;

    @OneToMany(mappedBy = "store")
    private List<TeamStore> teamStores = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<RefundRequest> refundRequests = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<OrderHistory> orderHistories = new ArrayList<>();

    public Store(CreateStoreReq createStoreReq) {
        this.address = createStoreReq.getAddress();
        this.latitude = createStoreReq.getLatitude();
        this.longitude = createStoreReq.getLongitude();
        this.storeName = createStoreReq.getStoreName();
        this.type = createStoreReq.getType();
        this.userRegisterPermission = createStoreReq.isUserRegisterPermission();
    }
}
