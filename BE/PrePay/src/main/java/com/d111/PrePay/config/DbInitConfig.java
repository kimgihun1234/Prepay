package com.d111.PrePay.config;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.model.DetailHistory;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.repository.*;
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
    private final OrderHistoryRepository orderHistoryRepository;
    private final StoreRepository storeRepository;
    private final TeamRepository teamRepository;
    private final DetailHistoryRepository detailHistoryRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) initData();
    }

    public void initData() {
        for (int i = 1; i < 15; i++) {
            String email = "user" + i + "@gmail.com";
            userService.userSignUp(new UserSignUpReq(email, email, "user" + i));
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
            teamService.createTeam(new TeamCreateReq(k, false, 10000, 0, k+"테스트메시지","FFFFFF"), i, null);
        }

        for (long i = 6; i < 11; i++) {
            String k = "team" + i;
            teamService.createTeam(new TeamCreateReq(k, true, 5000, 0, k+"publicteam 테스트메시지2","000000"), i, null);
        }
        log.info("team 삽입 완료");

        for (long i = 1; i < 3; i++) {
            for (long j = i + 2; j < i + 5; j++) {
                TeamCreateStoreReq req = new TeamCreateStoreReq();
                req.setStoreId(j);
                req.setTeamId(i);
                req.setBalance(100000);
                teamService.createStore(req,null);
            }
        }
        log.info("식당 팀에 삽입 완료");

        for (long i = 1; i < 5; i++) {
            for (long j = 7; j < 10; j++) {
                TeamIdReq teamId = new TeamIdReq(i);
                SignInTeamReq signInTeamReq = new SignInTeamReq();
                Long userId = userRepository.findUserByEmail("user" + j + "@gmail.com").getId();
                signInTeamReq.setTeamPassword(teamService.generateInviteCode(1L + i, teamId).getTeamPassword());
                teamService.signInTeam(1L + j, signInTeamReq);
            }
        }
        log.info("사람 팀에 삽입 완료");

        for (long i = 1; i < 5; i++) {
            for (long k = 1; k < 5; k++) {
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setStore(storeRepository.getReferenceById(k));
                orderHistory.setUser(userRepository.findById(i).orElseThrow());
                orderHistory.setOrderDate(System.currentTimeMillis()-1000*60*60-i);
                orderHistory.setTeam(teamRepository.findById(i).orElseThrow());
                orderHistory.setRefundRequested(false);
                orderHistory.setCompanyDinner(false);
                orderHistory.setWithDraw(false);
                orderHistory.setTotalPrice(21000);
                orderHistoryRepository.save(orderHistory);
                for (int j = 1; j < 6; j++) {
                    DetailHistory detailHistory = new DetailHistory();
                    detailHistory.setOrderHistory(orderHistory);
                    detailHistory.setProduct("제품명"+j);
                    detailHistory.setDetailPrice (j*1000);
                    detailHistory.setQuantity(j);
                    detailHistoryRepository.save(detailHistory);
                }
            }


        }

        log.info("주문 내역 삽입 완료");

    }
}
