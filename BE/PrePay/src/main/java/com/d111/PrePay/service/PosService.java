package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.DetailHistoryReq;
import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.exception.NotEnoughBalanceException;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import com.d111.PrePay.value.QrType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
    public Long makeOrder(OrderCreateReq orderReq) {
        Qr qr = qrRepository.findByUuid(orderReq.getQrUUID());

        if (qr.getGenDate() < System.currentTimeMillis() - 1000 * 60) {
            throw new RuntimeException("시간 초과");
        } else if (qr.isUsed()) {
            throw new RuntimeException("사용된 QR");
        }
        qr.setUsed(true);
        OrderHistory orderHistory = new OrderHistory(orderReq);
        Store store = storeRepository.findById(orderReq.getStoreId()).orElseThrow(() -> new RuntimeException("가게 오류"));

        UserTeam userTeam = userTeamRepository.findById(orderReq.getUserTeamId()).orElseThrow();
        userTeam.setUsedAmount(userTeam.getUsedAmount() + orderHistory.getTotalPrice());
        Team team = userTeam.getTeam();
        User user = userTeam.getUser();
        TeamStore teamStore = teamStoreRepository.findTeamStoreByTeamAndStore(team, store);

        teamStore.setTeamStoreBalance(teamStore.getTeamStoreBalance() - orderHistory.getTotalPrice());
        log.info("총 주문 금액 : {}",orderHistory.getTotalPrice());
        if (teamStore.getTeamStoreBalance() < 0) {
            teamStore.setTeamStoreBalance(teamStore.getTeamStoreBalance() + orderHistory.getTotalPrice());
            throw new NotEnoughBalanceException("팀 잔액이 부족합니다,");
        }
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

        try{
            fcmService.sendDataMessageTo(user.getFcmToken(),"완료","주문이 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return orderHistory.getId();
    }


}
