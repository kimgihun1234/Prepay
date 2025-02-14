package com.d111.PrePay.schedule;

import com.d111.PrePay.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyPriceLimitScheduler {

    private final UserTeamRepository userTeamRepository;


    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyPriceLimit() {
        log.info("유저 일일한도 초기화를 시작합니다.");
        int updatedRows = userTeamRepository.resetDailyPriceLimit();
        log.info("초기화된 유저수: {}", updatedRows);
    }

}
