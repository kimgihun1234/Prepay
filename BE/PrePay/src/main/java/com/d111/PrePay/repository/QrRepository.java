package com.d111.PrePay.repository;

import com.d111.PrePay.model.Qr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QrRepository extends JpaRepository<Qr,Long> {
    Qr findByUuid(String uuid);
}
