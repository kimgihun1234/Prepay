package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.DetailHistoryReq;
import com.d111.PrePay.dto.request.OrderReq;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PosService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;
    private final StoreRepository storeRepository;
    private final UserTeamRepository userTeamRepository;

    public Long makeOrder(OrderReq orderReq) {
        OrderHistory orderHistory = new OrderHistory(orderReq);
        Store store = storeRepository.findById(orderReq.getStoreId()).orElseThrow(() -> new RuntimeException("가게 오류"));
        UserTeam userTeam = userTeamRepository.findById(orderReq.getUserTeamId()).orElseThrow();
        userTeam.setUsedAmount(userTeam.getUsedAmount() + orderHistory.getTotalPrice());
        Team team = userTeam.getTeam();
        User user = userTeam.getUser();
        orderHistory.setStore(store);
        orderHistory.setTeam(team);
        orderHistory.setUser(user);
        orderHistoryRepository.save(orderHistory);

        for (DetailHistoryReq detailHistoryReq : orderReq.getDetails()) {
            DetailHistory detailHistory = new DetailHistory(detailHistoryReq);
            detailHistory.setOrderHistory(orderHistory);
            detailHistoryRepository.save(detailHistory);
        }


        return orderHistory.getId();
    }


}
