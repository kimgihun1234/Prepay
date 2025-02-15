package com.d111.PrePay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoresReq {
    private float latitude;
    private float longitude;
    private long teamId;
}
