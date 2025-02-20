package com.d111.PrePay.repository;

import com.d111.PrePay.dto.respond.OrderHistoryRes;
import com.d111.PrePay.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {
    List<OrderHistory> findOrderHistoriesByStoreIdEquals(long store_id);

    List<OrderHistory> findOrderHistoriesByStoreIdEqualsOrderByOrderDateDesc(long store_id);

    List<OrderHistory> findOrderHistoriesByTeamIdEquals(long team_id);

    List<OrderHistory> findOrderHistoriesByTeamIdEqualsOrderByOrderDateDesc(long team_id);

    List<OrderHistory> findOrderHistoriesByTeamIdEqualsAndStoreIdEquals(long team_id,long store_id);

    List<OrderHistory> findOrderHistoriesByTeamIdEqualsAndStoreIdEqualsOrderByOrderDateDesc(long team_id,long store_id);

    List<OrderHistory> findByUser_Email(String userEmail);
}
