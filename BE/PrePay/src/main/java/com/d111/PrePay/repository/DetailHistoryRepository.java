package com.d111.PrePay.repository;

import com.d111.PrePay.model.DetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DetailHistoryRepository extends JpaRepository<DetailHistory,Long> {
    List<DetailHistory> getDetailHistoriesByOrderHistoryId(long id);
}
