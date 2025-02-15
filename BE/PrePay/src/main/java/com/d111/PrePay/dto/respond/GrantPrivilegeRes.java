package com.d111.PrePay.dto.respond;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GrantPrivilegeRes {
    private String changeUserEmail;
    private Long teamId;
    private boolean privilege;

}
