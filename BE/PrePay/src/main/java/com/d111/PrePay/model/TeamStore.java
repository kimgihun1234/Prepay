package com.d111.PrePay.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class TeamStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_store_id")
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "teamStore")
    private List<ChargeRequest> chargeRequests = new ArrayList<>();

    private int teamStoreBalance;

    public TeamStore(Team team, Store store, int balance) {
        this.team = team;
        this.store = store;
        this.teamStoreBalance = balance;
    }
}
