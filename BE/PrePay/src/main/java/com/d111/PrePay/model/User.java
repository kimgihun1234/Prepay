package com.d111.PrePay.model;


import com.d111.PrePay.dto.request.UserSignUpReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    private String email;

    private String userPassword;

    private String nickname;

    private String fcmToken;

    private Long kakaoId;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RefundRequest> refundRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<OrderHistory> orderHistories = new ArrayList<>();

    public User(UserSignUpReq userSignUpReq) {
        this.email = userSignUpReq.getEmail();
        this.userPassword = userSignUpReq.getPassword();
        this.nickname = userSignUpReq.getNickname();
    }

}
