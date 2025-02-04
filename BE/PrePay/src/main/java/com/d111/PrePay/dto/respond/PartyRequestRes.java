package com.d111.PrePay.dto.respond;

import com.d111.PrePay.RequestStatus;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PartyRequestRes {
    private Long partyRequestId;
    private Long requestDate;
    private Enum<RequestStatus> requestStatus;
    private Long statusChangedDate;

}
