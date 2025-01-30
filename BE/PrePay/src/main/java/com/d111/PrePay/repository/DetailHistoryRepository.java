package com.d111.PrePay.repository;

import com.d111.PrePay.model.DetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface DetailHistoryRepository extends JpaRepository<DetailHistory,Long> {
}
