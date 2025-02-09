package com.d111.PrePay.bootpay;

import com.d111.PrePay.bootpay.util.Bootpay;
import com.d111.PrePay.dto.request.BootChargeReq;
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


    public boolean makeCharge(BootChargeReq bootChargeReq) {
        Bootpay api = new Bootpay(REST_APPLICATION_ID, PRIVATE_KEY);
        setBootpayToken(api);
        try {
            HttpResponse res = api.verify(String.valueOf(bootChargeReq.getReceiptId()));
            String str = StreamUtils.copyToString(res.getEntity().getContent(), StandardCharsets.UTF_8);
            log.info("검증 : {}", str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
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
