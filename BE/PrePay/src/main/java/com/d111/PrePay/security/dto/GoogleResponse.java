package com.d111.PrePay.security.dto;

import java.util.Map;

public class GoogleResponse implements Oauth2Response{

    private final Map<String,Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
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
