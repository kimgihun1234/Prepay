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
public class UserTeam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_team_id")
    private Long id;

    private boolean position;

    private boolean privilege;

    private int usedAmount;

    private int usageCount;

    private boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userTeam", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<PartyRequest> partyRequests = new ArrayList<>();



}
