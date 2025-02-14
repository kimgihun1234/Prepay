package com.d111.PrePay.bootpay;


import com.d111.PrePay.bootpay.response.PaymentResponse;
import com.d111.PrePay.dto.request.BootChargeReq;
import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BootpayController {

    private final BootpayService bootpayService;

    @PostMapping("/bootpay-charge")
    public ResponseEntity<StandardRes> makeCharge(@RequestHeader String access, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody BootChargeReq bootChargeReq) {
        String email = userDetails.getUsername();
        PaymentResponse result = bootpayService.makeCharge(bootChargeReq , email);

        return ResponseEntity.ok(new StandardRes(result.getData().getStatusKo(),200));
    }
}
