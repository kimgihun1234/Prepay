package com.d111.PrePay.bootpay;


import com.d111.PrePay.dto.request.BootChargeReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BootpayController {

    private final BootpayService bootpayService;

    @GetMapping("/test")
    public void test(BootChargeReq bootChargeReq) {
        bootpayService.makeCharge(bootChargeReq);
    }
}
