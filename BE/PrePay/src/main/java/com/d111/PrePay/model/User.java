package com.d111.PrePay.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userLoginId;

    private String userName;

    private String email;

    private String userPassword;

    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RefundRequest> refundRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<OrderHistory> orderHistories = new ArrayList<>();

}
