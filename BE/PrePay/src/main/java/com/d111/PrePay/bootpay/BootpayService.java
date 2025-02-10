package com.d111.PrePay.bootpay;

import com.d111.PrePay.bootpay.response.PaymentResponse;
import com.d111.PrePay.bootpay.util.Bootpay;
import com.d111.PrePay.dto.request.BootChargeReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class BootpayService {
    private Long bootpayTokenDate = null;
    @Value("${bootpay.appid}")
    private String REST_APPLICATION_ID;
    @Value("${bootpay.key}")
    private String PRIVATE_KEY;


    public PaymentResponse makeCharge(BootChargeReq bootChargeReq) {
        Bootpay api = new Bootpay(REST_APPLICATION_ID, PRIVATE_KEY);
        log.info("REST ID : {}", REST_APPLICATION_ID);
        log.info("PRIVATE_KEY : {}", PRIVATE_KEY);
        PaymentResponse response;
        setBootpayToken(api);
        try {
            HttpResponse res = api.verify(String.valueOf(bootChargeReq.getReceiptId()));
            String str = StreamUtils.copyToString(res.getEntity().getContent(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            response = mapper.readValue(str, PaymentResponse.class);
            log.info("결제 상태 {}", response.getData().getStatusKo());
            log.info("결제 금액 {}", response.getData().getPrice());
            log.info("검증 : {}", str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public void setBootpayToken(Bootpay api) {
        if (api.token != null) {
            return;
        } else if (bootpayTokenDate != null && bootpayTokenDate + (1800 * 1000) > System.currentTimeMillis()) {
            return;
        }
        try {
            api.getAccessToken();
            log.info("bootpay access token : {}", api.token);
            bootpayTokenDate = System.currentTimeMillis();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
