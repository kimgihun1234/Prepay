package com.d111.PrePay;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.model.OrderHistory;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.repository.OrderHistoryRepository;
import com.d111.PrePay.repository.TeamStoreRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@ActiveProfiles("test")
class PrePayApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(PrePayApplicationTests.class);
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PosService posService;
    @Autowired
    private QrService qrService;
    @Autowired
    private TeamStoreRepository teamStoreRepository;
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Test
    void sameTime() throws InterruptedException {
        String email = "user1@gmail.com";
        userService.userSignUp(new UserSignUpReq(email, email, "user1"));
        email = "user2@gmail.com";
        userService.userSignUp(new UserSignUpReq(email, email, "user2"));
        storeService.makeStore(new CreateStoreReq("store1", "식당", false, "주소1", 36.0084F, 128.015F));
        teamService.createTeam(new TeamCreateReq("팀이름1", false, 1000, 0, "publicteam 테스트메시지2", "000000"), 1L, null);

        TeamCreateStoreReq req = new TeamCreateStoreReq();
        req.setStoreId(1L);
        req.setTeamId(1L);
        teamService.createStore(req);

        TeamIdReq teamId = new TeamIdReq(1L);
        SignInTeamReq signInTeamReq = new SignInTeamReq();
        signInTeamReq.setTeamPassword(teamService.generateInviteCode(1L + 1, teamId).getInviteCode());
        teamService.signInTeam(1L + 1, signInTeamReq);


        DetailHistoryReq detailHistoryReq = new DetailHistoryReq();
        detailHistoryReq.setDetailPrice(1000);
        detailHistoryReq.setQuantity(1);
        detailHistoryReq.setProduct("상품");

        String qr1 = qrService.getPrivateQr("user1@gmail.com", 1L).getMessage();
        String qr2 = qrService.getPrivateQr("user2@gmail.com", 1L).getMessage();
        OrderCreateReq orderCreateReq1 = new OrderCreateReq();
        orderCreateReq1.setTeamId(1L);
        orderCreateReq1.setStoreId(1L);
        orderCreateReq1.setQrUUID(qr1);
        List<DetailHistoryReq> list = new ArrayList<>();
        list.add(detailHistoryReq);
        orderCreateReq1.setDetails(list);

        OrderCreateReq orderCreateReq2 = new OrderCreateReq();
        orderCreateReq2.setTeamId(1L);
        orderCreateReq2.setStoreId(1L);
        orderCreateReq2.setQrUUID(qr2);
        orderCreateReq2.setDetails(list);


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executorService.submit(() -> {
            executorService.submit(() -> {
                try {
                    posService.makeOrder(orderCreateReq1,"user1@gmail.com");
                } finally {
                    latch.countDown();
                }
            });

            executorService.submit(() -> {
                try {
                    posService.makeOrder(orderCreateReq2,"user2@gmail.com");
                } finally {
                    latch.countDown();
                }
            });


        });
        latch.await();
        executorService.shutdown();

        List<OrderHistory> all = orderHistoryRepository.findAll();
        log.info("주문 갯수 : {}", all.size());
        TeamStore teamStore = teamStoreRepository.findById(1L).get();
        Assertions.assertEquals(0, teamStore.getTeamStoreBalance());
    }


}
