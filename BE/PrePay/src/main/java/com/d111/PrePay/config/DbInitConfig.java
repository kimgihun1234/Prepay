package com.d111.PrePay.config;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.model.DetailHistory;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.repository.*;
import com.d111.PrePay.service.StoreService;
import com.d111.PrePay.service.TeamService;
import com.d111.PrePay.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile({"local","prod"})
@RequiredArgsConstructor
@Slf4j
public class DbInitConfig implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) initData();
    }

    public void initData() {


        userService.userSignUp(new UserSignUpReq("1@gmail.com", "123", "김기훈"));
        userService.userSignUp(new UserSignUpReq("2@gmail.com", "123", "조성윤"));
        userService.userSignUp(new UserSignUpReq("3@gmail.com", "123", "차현우"));
        userService.userSignUp(new UserSignUpReq("4@gmail.com", "123", "서현석"));
        userService.userSignUp(new UserSignUpReq("5@gmail.com", "123", "경이현"));
        userService.userSignUp(new UserSignUpReq("6@gmail.com", "123", "김성수"));
        userService.userSignUp(new UserSignUpReq("7@gmail.com", "123", "이재용"));
        userService.userSignUp(new UserSignUpReq("8@gmail.com", "123", "아이유"));
        userService.userSignUp(new UserSignUpReq("9@gmail.com", "123", "반짝이는바지"));
        userService.userSignUp(new UserSignUpReq("10@gmail.com", "123", "싸피최고컨설턴트"));
        userService.userSignUp(new UserSignUpReq("11@gmail.com", "123", "구미꿀주먹"));
        userService.userSignUp(new UserSignUpReq("12@gmail.com", "123", "배고픈취준생"));
        log.info("사람 넣기 완료");



        ////////////////////////////////////////////////////////////////////////

        /*for (int i = 1; i < 15; i++) {
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

        for (long i = 1; i < 10; i++) {
            List<Store> stores = storeRepository.findAll();
            for (Store store : stores) {
                store.setStoreImgUrl("https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/02ef8733-a852-4a86-b482-28c4e65b30d4_images.png?alt=media&token=7335964c-3b70-4a5c-8cba-247721fcd9b4");
            }
        }

        log.info("store 삽입 완료");

        for (long i = 1; i < 6; i++) {
            String k = "team" + i;
            teamService.createTeam(new TeamCreateReq(k, false, 1000000, 0, k+"테스트메시지","FFFFFF"), i, null);
        }

        for (long i = 6; i < 11; i++) {
            String k = "team" + i;
            teamService.createTeam(new TeamCreateReq(k, true, 500000, 0, k+"publicteam 테스트메시지2","000000"), i, null);
        }

        for (long i = 1; i < 11; i++) {
            Optional<Team> opTeam = teamRepository.findById(i);
            Team team = opTeam.get();
            team.setTeamImgUrl("https://storage.googleapis.com/kyung0216-10d14.appspot.com/e7b077fd-bbdf-47d5-9de8-934f4cccfddd_example.jpg");
        }

        log.info("team 삽입 완료");

        for (long i = 1; i < 3; i++) {
            for (long j = i + 2; j < i + 5; j++) {
                TeamCreateStoreReq req = new TeamCreateStoreReq();
                req.setStoreId(j);
                req.setTeamId(i);
                req.setBalance(100000000);
                teamService.createStore(req,null);
            }
        }
        TeamCreateStoreReq req = new TeamCreateStoreReq();
        req.setStoreId(1L);
        req.setTeamId(10L);
        req.setBalance(100000000);
        teamService.createStore(req,null);

        for (long l = 3; l < 10; l++) {
            for (long k = 4; k < 10; k++) {
                req = new TeamCreateStoreReq();
                req.setStoreId(l);
                req.setTeamId(k);
                req.setBalance(100000000);
                teamService.createStore(req,null);
            }
        }

        log.info("식당 팀에 삽입 완료");

        for (long i = 1; i < 5; i++) {
            for (long j = 7; j < 10; j++) {
                TeamIdReq teamId = new TeamIdReq(i);
                SignInTeamReq signInTeamReq = new SignInTeamReq();
                Long userId = userRepository.findUserByEmail("user" + j + "@gmail.com").getId();
                signInTeamReq.setTeamPassword(teamService.generateInviteCode(1L + i, teamId).getInviteCode());
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

        log.info("주문 내역 삽입 완료");*/

    }
}
