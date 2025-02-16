package com.d111.PrePay.bootpay.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetail {
    @JsonProperty("card_no")
    private String cardNumber;

    @JsonProperty("card_quota")
    private String cardQuota;

    @JsonProperty("card_type")
    private int cardType;

    // 거래 정보
    @JsonProperty("receipt_id")
    private String receiptId;
    private String n;
    private double p;

    @JsonProperty("tid")
    private String transactionId;
    private String pg;
    private String pm;

    @JsonProperty("pg_a")
    private String pgAlias;

    @JsonProperty("pm_a")
    private String pmAlias;

    @JsonProperty("o_id")
    private String orderId;

    @JsonProperty("p_at")
    private String paymentAt;
    private int s;
    private int g;

    @JsonProperty("naver_point")
    private int naverPoint;
}
