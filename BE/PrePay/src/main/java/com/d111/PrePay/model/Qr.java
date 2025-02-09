package com.d111.PrePay.model;

import com.d111.PrePay.value.QrType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Qr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private long genDate;

    private QrType type;

    @ManyToOne
    private User user;

    public Qr(QrType type, User user) {
        this.type = type;
        this.genDate = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString();
        this.user = user;
    }
}