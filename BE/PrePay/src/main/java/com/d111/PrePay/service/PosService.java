package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.DetailHistoryReq;
import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.exception.NoQrException;
import com.d111.PrePay.exception.NotEnoughBalanceException;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import com.d111.PrePay.value.QrType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PosService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;
    private final StoreRepository storeRepository;
    private final UserTeamRepository userTeamRepository;
    private final QrRepository qrRepository;
    private final TeamStoreRepository teamStoreRepository;
    private final FCMService fcmService;

    @Transactional
    public Long makeOrder(OrderCreateReq orderReq, String email) {
        Qr qr = qrRepository.findByUuid(orderReq.getQrUUID()).orElseThrow(() -> {
            log.error("qr uuid : {}", orderReq.getQrUUID());
            throw new NoQrException("존재하지 않는 qr");
        });

        if (qr.getGenDate() < System.currentTimeMillis() - 1000 * 60) {
            throw new RuntimeException("시간 초과");
        } else if (qr.isUsed()) {
            throw new RuntimeException("사용된 QR");
        }
        qr.setUsed(true);
        OrderHistory orderHistory = new OrderHistory(orderReq);
        Store store = storeRepository.findById(orderReq.getStoreId()).orElseThrow(() ->
        {
            log.error("가게 아이디 : {}", orderReq.getStoreId());
            return new NoSuchElementException("가게 없음");
        });

        UserTeam userTeam = userTeamRepository.findByTeamIdAndUser_Email(orderReq.getTeamId(), email).orElseThrow(() ->
        {
            log.error("팀 아아디 : {}, 이메일 : {}", orderReq.getTeamId(), email);
            return new RuntimeException("userTeam 찾기 실패");
        });

        Team team = userTeam.getTeam();
        User user = userTeam.getUser();
        TeamStore teamStore = teamStoreRepository.findTeamStoreByTeamAndStore(team, store);

        log.info("총 주문 금액 : {}", orderHistory.getTotalPrice());
        if (teamStore.getTeamStoreBalance() - orderHistory.getTotalPrice() < 0) {
            log.error("주문자 : {}", user.getEmail());
            log.error("팀 잔액 : {},현재주문 금액 : {}", teamStore.getTeamStoreBalance(), orderHistory.getTotalPrice());
            throw new NotEnoughBalanceException("팀 잔액이 부족합니다,");
        } else if (qr.getType() == QrType.PRIVATE && team.getDailyPriceLimit() - userTeam.getUsedAmount() < orderHistory.getTotalPrice()) {
            log.error("일일한도 잔액 : {},현재주문 금액 : {}", team.getDailyPriceLimit() - userTeam.getUsedAmount(), orderHistory.getTotalPrice());
            throw new NotEnoughBalanceException("일일 한도 잔액이 부족합니다,");
        }

        teamStore.setTeamStoreBalance(teamStore.getTeamStoreBalance() - orderHistory.getTotalPrice());
        userTeam.setUsedAmount(userTeam.getUsedAmount() + orderHistory.getTotalPrice());
        orderHistory.setCompanyDinner(qr.getType() != QrType.PRIVATE);
        orderHistory.setStore(store);
        orderHistory.setTeam(team);
        orderHistory.setUser(user);
        orderHistoryRepository.save(orderHistory);
        for (DetailHistoryReq detailHistoryReq : orderReq.getDetails()) {
            DetailHistory detailHistory = new DetailHistory(detailHistoryReq);
            detailHistory.setOrderHistory(orderHistory);
            detailHistoryRepository.save(detailHistory);
        }

        try {
            fcmService.sendDataMessageTo(user.getFcmToken(), "완료", "주문이 완료되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("fcm 오류 발생");
        }

        return orderHistory.getId();
    }


}
