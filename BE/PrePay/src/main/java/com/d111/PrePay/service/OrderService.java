package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistoryRes> getOrderHistory(OrderHistoryReq orderHistoryReq) {
        if (orderHistoryReq.getStoreId() != null) {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByStoreIdEquals(orderHistoryReq.getStoreId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        } else {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByTeamIdEquals(orderHistoryReq.getTeamId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        }
    }
}
