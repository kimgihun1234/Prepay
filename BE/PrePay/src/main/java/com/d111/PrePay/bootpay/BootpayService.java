package com.d111.PrePay.bootpay;

import com.d111.PrePay.bootpay.response.PaymentResponse;
import com.d111.PrePay.bootpay.util.Bootpay;
import com.d111.PrePay.dto.request.BootChargeReq;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.TeamStoreRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.service.FCMService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootpayService {
    private final UserRepository userRepository;
    private final TeamStoreRepository teamStoreRepository;
    private Long bootpayTokenDate = null;
    private String bootpayToken;
    @Value("${bootpay.appid}")
    private String REST_APPLICATION_ID;
    @Value("${bootpay.key}")
    private String PRIVATE_KEY;

    private final FCMService fcmService;

    @Transactional
    public PaymentResponse makeCharge(BootChargeReq bootChargeReq, String email) {
        Bootpay api = new Bootpay(REST_APPLICATION_ID, PRIVATE_KEY);
        log.info("가게 아이디 : {}, 팀 아이디 :  {}, 영수중 아이디 : {}, 충전금액 : {}",bootChargeReq.getStoreId(),bootChargeReq.getTeamId(),bootChargeReq.getReceiptId(),bootChargeReq.getAmount());
        log.info("REST ID : {}", REST_APPLICATION_ID);
        log.info("PRIVATE_KEY : {}", PRIVATE_KEY);
        log.info("영수증 id : {}",bootChargeReq.getReceiptId());
        PaymentResponse response = new PaymentResponse();
        setBootpayToken(api);
        try {
            HttpResponse res = api.verify(String.valueOf(bootChargeReq.getReceiptId()));
            String str = StreamUtils.copyToString(res.getEntity().getContent(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            response = mapper.readValue(str, PaymentResponse.class);
            log.info("결과 메세지 값 : {}" ,response.getMessage());
            log.info("결제 상태 {}", response.getData().getStatusKo());
            log.info("결제 금액 {}", response.getData().getPrice());
            log.info("검증 : {}", str);
        } catch (Exception e) {
            throw new RuntimeException("결제 상태 "+ response.getMessage());
        }
        TeamStore teamStore = teamStoreRepository.findByTeamIdAndStoreId(bootChargeReq.getTeamId(), bootChargeReq.getStoreId()).orElseThrow();
        teamStore.setTeamStoreBalance(teamStore.getTeamStoreBalance() + bootChargeReq.getAmount());
        String storeName = teamStore.getStore().getStoreName();
        String teamName = teamStore.getTeam().getTeamName();
        User user = userRepository.findUserByEmail(email);
        log.info(user.getFcmToken());
        try {
            fcmService.sendDataMessageTo(user.getFcmToken(), "완료", "금액 : " + response.getData().getPrice() + "원 " + teamName + " 그룹의 " + storeName + " 가게에 " + "충전이 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return response;
    }

    public void setBootpayToken(Bootpay api) {
        if (api.token != null) {
            return;
        } /*else if (bootpayTokenDate != null && bootpayTokenDate + (1800 * 1000) > System.currentTimeMillis()) {
            return;
        }*/
        try {
            api.getAccessToken();
            bootpayToken = api.token;
            log.info("bootpay access token : {}", api.token);
            bootpayTokenDate = System.currentTimeMillis();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
