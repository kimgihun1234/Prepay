package com.d111.PrePay.repository;

import com.d111.PrePay.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory,Long> {
}
