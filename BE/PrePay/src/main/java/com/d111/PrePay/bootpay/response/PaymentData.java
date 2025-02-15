package com.d111.PrePay.bootpay.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentData {
    // 결제 기본 정보
    @JsonProperty("receipt_id")
    private String receiptId;

    @JsonProperty("order_id")
    private String orderId;
    private String name;

    @JsonProperty("item_name")
    private String itemName;
    private int price;

    @JsonProperty("tax_free")
    private int taxFree;

    // 잔액 정보
    @JsonProperty("remain_price")
    private double remainPrice;

    @JsonProperty("remain_tax_free")
    private int remainTaxFree;

    @JsonProperty("cancelled_price")
    private double cancelledPrice;

    @JsonProperty("cancelled_tax_free")
    private int cancelledTaxFree;

    // 결제 수단 정보
    @JsonProperty("receipt_url")
    private String receiptUrl;
    private String unit;
    private String pg;
    private String method;

    @JsonProperty("pg_name")
    private String pgName;

    @JsonProperty("method_name")
    private String methodName;

    // 상세 결제 데이터
    @JsonProperty("payment_data")
    private PaymentDetail paymentData;

    // 타임스탬프
    @JsonProperty("requested_at")
    private String requestedAt;

    @JsonProperty("purchased_at")
    private String purchasedAt;

    // 결제 상태
    private int status;

    @JsonProperty("status_en")
    private String statusEn;

    @JsonProperty("status_ko")
    private String statusKo;
}
