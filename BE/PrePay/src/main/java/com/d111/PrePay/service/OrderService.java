package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.request.RefundRequestCreateReq;
import com.d111.PrePay.dto.respond.DetailHistoryRes;
import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.DetailHistoryRepository;
import com.d111.PrePay.repository.OrderHistoryRepository;
import com.d111.PrePay.repository.RefundRequestRepository;
import com.d111.PrePay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;
    private final RefundRequestRepository refundRequestRepository;
    private final UserRepository userRepository;

    public List<OrderHistoryRes> getOrderHistory(OrderHistoryReq req) {
        if (req.getStoreId() == 0 && req.getTeamId() == 0) {
            List<OrderHistory> originalList = orderHistoryRepository.findAll();
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        } else if (req.getStoreId() == 0) {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByStoreIdEquals(req.getTeamId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        } else if (req.getTeamId() == 0) {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByTeamIdEquals(req.getStoreId());
            List<OrderHistoryRes> resultList = new ArrayList<>();
            for (OrderHistory orderHistory : originalList) {
                OrderHistoryRes result = new OrderHistoryRes(orderHistory);
                resultList.add(result);
            }
            return resultList;
        } else {
            List<OrderHistory> originalList = orderHistoryRepository.findOrderHistoriesByTeamIdEqualsAndStoreIdEquals(req.getTeamId(), req.getStoreId());
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

    public Long makeRefundRequest(RefundRequestCreateReq req) {
        OrderHistory orderHistory = orderHistoryRepository.findById(req.getOrderHistoryId()).orElseThrow();
        RefundRequest refundRequest = new RefundRequest(orderHistory);
        refundRequestRepository.save(refundRequest);
        return refundRequest.id;
    }

    public List<OrderHistoryRes> getMyOrderHistory(String email) {
        List<OrderHistory> orderList = orderHistoryRepository.findByUser_Email(email);
        List<OrderHistoryRes> result = new ArrayList<>();
        for (OrderHistory orderHistory : orderList) {
            result.add(new OrderHistoryRes(orderHistory));
        }
        return result;
    }
}