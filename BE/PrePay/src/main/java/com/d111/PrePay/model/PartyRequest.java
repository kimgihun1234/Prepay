package com.d111.PrePay.model;

import com.d111.PrePay.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PartyRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_request_id")
    private long id;

    private long requestDate;


    private RequestStatus requestStatus;

    private long statusChangedDate;

    @ManyToOne
    private UserTeam userTeam;
}
