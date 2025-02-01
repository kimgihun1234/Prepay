package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GrantAdminPositionReq {
    private Long changeUserId;
    private Long userId;
    private Long teamId;
    private boolean position;
}
