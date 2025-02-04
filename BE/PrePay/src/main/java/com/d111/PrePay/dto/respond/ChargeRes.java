package com.d111.PrePay.dto.respond;

import com.d111.PrePay.RequestStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargeRes {
    private Long chargeRequestId;
    private Enum<RequestStatus> RequestStatus;
    private int requestPrice;
    private Long requestDate;

}
