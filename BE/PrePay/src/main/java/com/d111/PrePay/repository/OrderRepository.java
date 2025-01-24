package com.d111.PrePay.repository;

import com.d111.PrePay.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderHistory,Long> {
}
