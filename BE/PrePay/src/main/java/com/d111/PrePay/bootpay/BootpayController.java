package com.d111.PrePay.bootpay;


import com.d111.PrePay.bootpay.response.PaymentResponse;
import com.d111.PrePay.dto.request.BootChargeReq;
import com.d111.PrePay.dto.respond.StandardRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BootpayController {

    private final BootpayService bootpayService;

    @PostMapping("/bootpay-charge")
    public ResponseEntity<StandardRes> makeCharge(BootChargeReq bootChargeReq) {
        PaymentResponse result = bootpayService.makeCharge(bootChargeReq);
        return ResponseEntity.ok(new StandardRes(result.getData().getStatusKo(),200));
    }
}
