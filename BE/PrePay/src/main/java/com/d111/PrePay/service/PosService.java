package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.DetailHistoryReq;
import com.d111.PrePay.dto.request.OrderCreateReq;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import com.d111.PrePay.value.QrType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;
    private final StoreRepository storeRepository;
    private final UserTeamRepository userTeamRepository;
    private final QrRepository qrRepository;

    public Long makeOrder(OrderCreateReq orderReq) {
        Qr qr = qrRepository.findByUuid(orderReq.getQrUUID());

        /*if (qr.getGenDate() > System.currentTimeMillis() - 1000 * 30) {
            추후 error handleing 구현 후 에러 만들 것
        }*/

        OrderHistory orderHistory = new OrderHistory(orderReq);
        Store store = storeRepository.findById(orderReq.getStoreId()).orElseThrow(() -> new RuntimeException("가게 오류"));
        UserTeam userTeam = userTeamRepository.findById(orderReq.getUserTeamId()).orElseThrow();
        userTeam.setUsedAmount(userTeam.getUsedAmount() + orderHistory.getTotalPrice());
        Team team = userTeam.getTeam();
        User user = userTeam.getUser();
        if(qr.getType()== QrType.PRIVATE){
            orderHistory.setCompanyDinner(false);
        }else{
            orderHistory.setCompanyDinner(true);
        }
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
