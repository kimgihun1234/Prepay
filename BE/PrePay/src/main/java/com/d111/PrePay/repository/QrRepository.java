package com.d111.PrePay.repository;

import com.d111.PrePay.model.Qr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrRepository extends JpaRepository<Qr,Long> {
    Optional<Qr> findByUuid(String uuid);
}
