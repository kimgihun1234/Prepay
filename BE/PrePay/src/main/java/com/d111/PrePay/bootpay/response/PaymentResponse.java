package com.d111.PrePay.bootpay.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {
    private int status;
    private int code;
    private String message;
    private PaymentData data;
}
