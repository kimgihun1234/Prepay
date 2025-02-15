package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.OrderHistoryReq;
import com.d111.PrePay.dto.respond.*;
import com.d111.PrePay.model.DetailHistory;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.repository.DetailHistoryRepository;
import com.d111.PrePay.repository.OrderHistoryRepository;
import com.d111.PrePay.repository.TeamStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final TeamStoreRepository teamStoreRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final DetailHistoryRepository detailHistoryRepository;

    // 사장님 가게에 선결제한 팀들 보기
    public List<GetPrepaidTeamsRes> getPrepaidTeams(Long storeId) {
        List<TeamStore> teamStores = teamStoreRepository.findByStoreId(storeId);
        List<GetPrepaidTeamsRes> resultList = new ArrayList<>();

        for (TeamStore teamStore : teamStores) {
            GetPrepaidTeamsRes res = new GetPrepaidTeamsRes(teamStore, teamStore.getTeam());
            resultList.add(res);
        }

        return resultList;
    }

    // 가게 주문 내역 보기
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

}
