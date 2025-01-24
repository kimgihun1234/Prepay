package com.d111.PrePay.repository;

import com.d111.PrePay.model.ChargeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRequestRepository extends JpaRepository<ChargeRequest,Long> {
}
