package com.d111.PrePay.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private boolean publicTeam;

    private String teamPassword;

    private int dailyPriceLimit;

    private int countLimit;

    private int teamBalance;

    private String teamMessage;

    private String teamImgUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User teamInitializer;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamStore> teamStores = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<OrderHistory> orderHistories = new ArrayList<>();

}
