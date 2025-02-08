package com.d111.PrePay.config;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.service.StoreService;
import com.d111.PrePay.service.TeamService;
import com.d111.PrePay.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbInitConfig implements ApplicationRunner {

    private final UserService userService;
    private final TeamService teamService;
    private final UserRepository userRepository;
    private final StoreService storeService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) initData();
    }

    private void initData() {
        for (int i = 1; i < 15; i++) {
            String k = "user" + i + "@google.com";
            userService.userSignUp(new UserSignUpReq(k, k, k, k));
        }
        log.info("유저 삽입 완료");
        for (int i = 1; i < 6; i++) {
            String k = "store" + i;
            storeService.makeStore(new CreateStoreReq(k, "식당", false, k, 36.1084F, 128.415F));
        }
        for (int i = 6; i < 11; i++) {
            String k = "store" + i;
            storeService.makeStore(new CreateStoreReq(k, "식당", false, k, 36.0084F, 128.015F));
        }
        log.info("store 삽입 완료");

        for (long i = 1; i < 6; i++) {
            String k = "team" + i;
            teamService.createTeam(new TeamCreateReq(k, false, 10000, 0, "테스트메시지"), i, null);
        }

        for (long i = 6; i < 11; i++) {
            String k = "team" + i;
            teamService.createTeam(new TeamCreateReq(k, true, 5000, 0, "테스트메시지2"), i, null);
        }
        log.info("team 삽입 완료");

        for (long i = 1; i < 3; i++) {
            for (long j = i + 2; j < i + 5; j++) {
                TeamCreateStoreReq req = new TeamCreateStoreReq();
                req.setStoreId(j);
                req.setTeamId(i);
                req.setBalance(100000);
                teamService.createStore(req);
            }
        }
        log.info("식당 팀에 삽입 완료");

        for (long i = 1; i < 3; i++) {
            for (long j = 5; j < 8; j++) {
                TeamIdReq teamId = new TeamIdReq(i);
                SignInTeamReq signInTeamReq = new SignInTeamReq();
                signInTeamReq.setTeamPassword(teamService.generateInviteCode(1L + i, teamId).getTeamPassword());
                teamService.signInTeam(1L + j, signInTeamReq);
            }
        }
        log.info("사람 팀에 삽입 완료");
    }
}
