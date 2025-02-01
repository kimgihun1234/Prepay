package com.d111.PrePay.model;

import com.d111.PrePay.RequestStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder
public class PartyRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_request_id")
    private Long id;

    private long requestDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private long statusChangedDate;

    @ManyToOne
    private UserTeam userTeam;
}
