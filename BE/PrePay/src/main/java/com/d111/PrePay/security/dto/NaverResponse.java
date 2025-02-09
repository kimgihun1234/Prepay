package com.d111.PrePay.security.dto;

import java.util.Map;

public class NaverResponse implements Oauth2Response{


    private final Map<String,Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
