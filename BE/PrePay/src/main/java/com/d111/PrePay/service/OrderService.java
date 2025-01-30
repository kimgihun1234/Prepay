package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.respond.DetailHistoryRes;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.model.DetailHistory;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.repository.DetailHistoryRepository;
import com.d111.PrePay.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;

    public List<OrderHistoryRes> getOrderHistory(OrderHistoryReq req) {
        if (req.getStoreId() != null) {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByStoreIdEquals(req.getStoreId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        } else {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByTeamIdEquals(req.getTeamId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        }

    }

    public List<DetailHistoryRes> getDetailHistory(long detailHistoryId) {
        List<DetailHistory> originalList = detailHistoryRepository.getDetailHistoriesByOrderHistoryId(detailHistoryId);
        List<DetailHistoryRes> resultList = new ArrayList<>();
        for (DetailHistory detailHistory : originalList) {
            DetailHistoryRes detailHistoryRes = new DetailHistoryRes(detailHistory);
            resultList.add(detailHistoryRes);
        }
        return resultList;
    }
}