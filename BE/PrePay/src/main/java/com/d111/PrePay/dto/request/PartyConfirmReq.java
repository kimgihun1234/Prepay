package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyConfirmReq {
    private Long partyRequestId;
    private boolean accept;
}
