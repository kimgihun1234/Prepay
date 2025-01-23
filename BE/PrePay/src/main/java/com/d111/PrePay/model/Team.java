package com.d111.PrePay.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private long id;

    private String teamName;

    private boolean publicTeam;

    private String teamPassword;

    private int dailyPriceLimit;

    private int countLimit;

    private int teamBalance;

    private String teamMessage;

    private long teamInitializer;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamStore> teamStores = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<OrderHistory> orderHistories = new ArrayList<>();

}
