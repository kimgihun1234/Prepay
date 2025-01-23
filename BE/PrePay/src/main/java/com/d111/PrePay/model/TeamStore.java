package com.d111.PrePay.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TeamStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_store_id")
    private long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "teamStore")
    private List<ChargeRequest> chargeRequests = new ArrayList<>();

    private int teamStoreBalance;

}
