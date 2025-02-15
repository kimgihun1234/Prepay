package com.d111.PrePay.dto.respond;

import com.d111.PrePay.value.RequestStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartyConfirmRes {
    private Long partyRequestId;
    private Enum<RequestStatus> requestStatus;


}




