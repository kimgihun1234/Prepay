package com.d111.PrePay.dto.respond;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GrantAdminPositionRes {
    private Long changeUserId;
    private Long teamId;
    private boolean position;
}
