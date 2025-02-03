package com.d111.PrePay.dto.respond;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UploadImageRes {
    private Long teamId;
    private String teamName;
    private String imgUrl;

}
