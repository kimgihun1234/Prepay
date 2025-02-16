package com.d111.PrePay.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private boolean publicTeam;

    private String teamPassword;

    private int dailyPriceLimit;

    private int countLimit;

    private String teamMessage;

    private String teamImgUrl;

    private long codeGenDate;

    private String color;

    private long genDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User teamInitializer;

    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<TeamStore> teamStores = new ArrayList<>();


    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<OrderHistory> orderHistories = new ArrayList<>();

}
