package com.d111.PrePay.model;


import com.d111.PrePay.dto.request.UserSignUpReq;
import com.d111.PrePay.dto.request.UserCreateReq;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public User(UserSignUpReq userSignUpReq) {
        this.userLoginId = userSignUpReq.getUserLoginId();
        this.userName = userSignUpReq.getUserName();
        this.email = userSignUpReq.getEmail();
        this.userPassword = userSignUpReq.getPassword();
        this.nickname = userSignUpReq.getNickname();
    }
    public User(UserCreateReq req) {
        this.userLoginId = req.getUserLoginId();
        this.userName = req.getUserName();
        this.email = req.getEmail();
        this.userPassword = req.getPassword();
        this.nickname = req.getNickname();
    }
}
