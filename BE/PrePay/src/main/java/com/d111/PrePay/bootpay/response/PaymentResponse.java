package com.d111.PrePay.bootpay.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private int status;
    private int code;
    private String message;
    private PaymentData data;
}
