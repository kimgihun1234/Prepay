package com.d111.PrePay.model;

import com.d111.PrePay.RequestStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder
public class ChargeRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_request_id")
    private Long id;
    
    private RequestStatus requestStatus;

    private int requestPrice;

    private long requestDate;

    @ManyToOne
    private TeamStore teamStore;

}
