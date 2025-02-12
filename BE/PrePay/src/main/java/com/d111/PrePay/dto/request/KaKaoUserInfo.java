package com.d111.PrePay.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class KaKaoUserInfo {
    private Long id;
    private String connected_at;
    private Map<String, String> properties;
    private Map<String, String> profile;

}
