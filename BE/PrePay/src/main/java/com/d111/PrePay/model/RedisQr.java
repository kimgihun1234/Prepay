package com.d111.PrePay.model;

import com.d111.PrePay.value.QrType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RedisHash("qr")
@NoArgsConstructor
public class RedisQr implements Serializable {
    @Id
    private String uuid;
    private QrType type;
    private boolean used;
    private String userEmail;

    public RedisQr(QrType type, String userEmail) {
        this.type=type;
        this.used=false;
        this.userEmail=userEmail;
        this.uuid = UUID.randomUUID().toString();
    }
}
