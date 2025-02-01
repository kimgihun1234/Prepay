package com.d111.PrePay.service;

import com.d111.PrePay.RequestStatus;
import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.TeamStoreRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamStoreRepository teamStoreRepository;
    private final StoreRepository storeRepository;
    private final ChargeRequestRepository chargeRequestRepository;
    private final PartyRequestRepository partyRequestRepository;

    // 팀 나가기
    public void exitTeam(Long userId, TeamIdReq req){
       UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), userId)
               .orElseThrow();
       userTeamRepository.delete(findUserTeam);
    }


    // 팀 회식 권한 요청 처리
    @Transactional
    public void confirmPrivilege(PartyConfirmReq req){
        PartyRequest findPartyRequest = partyRequestRepository.findById(req.getPartyRequestId())
                .orElseThrow();
        if(req.isAccept()){
            findPartyRequest.setRequestStatus(RequestStatus.Approved);
        }
        else {
            findPartyRequest.setRequestStatus(RequestStatus.Refused);
        }
    }



    // 팀 회식 권한 요청
    public void privilegeRequest(Long userId, TeamIdReq req){
       UserTeam findUserTeam =  userTeamRepository.findByTeamIdAndUserId(req.getTeamId(),userId)
               .orElseThrow();
       partyRequestRepository.save(PartyRequest.builder()
                       .requestDate(System.currentTimeMillis())
                       .requestStatus(RequestStatus.Waiting)
                       .statusChangedDate(0)
                       .userTeam(findUserTeam)
                        .build());
    }


    // 팀 가맹점 잔액 충전 요청
    public void chargeRequest(ChargeReq req){
        TeamStore findTeamStore = teamStoreRepository.findByTeamIdAndStoreId(req.getTeamId(), req.getStoreId())
                .orElseThrow();
                chargeRequestRepository.save(ChargeRequest.builder()
                .requestStatus(RequestStatus.Waiting)
                .requestPrice(req.getRequestPrice())
                .requestDate(System.currentTimeMillis())
                .teamStore(findTeamStore)
                .build());

    }


    // 팀 비밀번호를 이용한 팀 가입
    public void signInTeam(Long userId, SignInTeamReq req){
        Team findTeam = teamRepository.findByTeamPassword(req.getTeamPassword())
                .orElseThrow(()-> new RuntimeException("일치하는 팀이 없습니다."));

        User findUser = userRepository.findById(userId).orElseThrow();

        if (userTeamRepository.existsByUserAndTeam(findUser,findTeam)){
            throw new RuntimeException("이미 가입된 팀입니다.");
        }
        UserTeam userTeam = UserTeam.builder()
                .team(findTeam)
                .user(findUser)
                .privilege(false)
                .usageCount(0)
                .usedAmount(0)
                .position(false)
                .build();
        userTeamRepository.save(userTeam);

    }




    // 팀 회식 권한 부여
    @Transactional
    public void grantPrivilege(GrantPrivilegeReq req){
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), req.getChangeUserId())
                .orElseThrow();
        findUserTeam.setPrivilege(req.isPrivilege());
    }



    // 팀 운영자 권한 부여
    @Transactional
    public void grantAdminPosition(GrantAdminPositionReq req){
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), req.getChangeUserId())
                .orElseThrow();
        findUserTeam.setPosition(req.isPosition());
    }



    // 팀 한도 변경
    @Transactional
    public Team changeDailyPriceLimit(ChangeDailyPriceLimitReq req){
        Team findTeam = teamRepository.findById(req.getTeamId()).orElseThrow();
        findTeam.setDailyPriceLimit(req.getDailyPriceLimit());
        return findTeam;
    }



    // 팀 초대 코드 생성
    public Team generateInviteCode(Long userId, TeamIdReq req){
        Team team = teamRepository.findById(req.getTeamId()).orElseThrow();
        String password = generateRandomPassword();
        team.setTeamPassword(password);
        return teamRepository.save(team);
    }


    // 팀 가맹점 추가
    public TeamStore createStore(TeamCreateStoreReq req){
        Team findTeam = teamRepository.findById(req.getTeamId()).orElseThrow();
        Store findStore = storeRepository.findById(req.getStoreId()).orElseThrow();

        TeamStore teamStore = new TeamStore(findTeam, findStore, req.getBalance());

        return teamStoreRepository.save(teamStore);
    }


    // 팀 유저 조회
    public List<GetUserOfTeamRes> getUsersOfTeam(Long teamId, Long userId) {
        Team findTeam = teamRepository.findById(teamId).orElseThrow();
        List<UserTeam> findUserTeams = findTeam.getUserTeams();
        List<GetUserOfTeamRes> result = new ArrayList<>();
        for (UserTeam findUserTeam : findUserTeams) {
            GetUserOfTeamRes getUserOfTeamRes = new GetUserOfTeamRes(findUserTeam);
            result.add(getUserOfTeamRes);
        }

        return result;

    }


    // 팀 상세 조회
    public TeamDetailRes getTeamDetails(Long teamId, Long userId) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new RuntimeException("유저팀을 찾을 수 없습니다."));

        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));
        TeamDetailRes res = TeamDetailRes.builder()
                .teamId(teamId)
                .teamName(findTeam.getTeamName())
                .dailyPriceLimit(findTeam.getDailyPriceLimit())
                .publicTeam(findTeam.isPublicTeam())
                .countLimit(findTeam.getCountLimit())
                .teamMessage(findTeam.getTeamMessage())
                .position(findUserTeam.isPosition())
                .build();

        return res;
    }


    // 팀 생성
    public Team createTeam(TeamCreateReq request) {
        String teamPassword;
        if (!request.isPublicTeam()) {
            teamPassword = generateRandomPassword();
        } else {
            teamPassword = null;
        }

        Team team = Team.builder()
                .teamName(request.getTeamName())
                .publicTeam(request.isPublicTeam())
                .teamPassword(teamPassword)
                .dailyPriceLimit(request.getDailyPriceLimit())
                .countLimit(request.getCountLimit())
                .teamMessage(request.getTeamMessage())
                .teamInitializer(request.getUserId())
                .build();


        Team savedTeam = teamRepository.save(team);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserTeam userTeam = UserTeam.builder()
                .team(savedTeam)
                .user(user)
                .position(true)
                .privilege(true)
                .usedAmount(0)
                .usageCount(0)
                .build();

        userTeamRepository.save(userTeam);

        return savedTeam;
    }

    //퍼블릭 팀 전체 조회
    public List<TeamRes> getMyTeams(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<UserTeam> userTeams = user.getUserTeams();
        List<TeamRes> resultList = new ArrayList<>();
        for (UserTeam userTeam : userTeams) {
            TeamRes teamRes = new TeamRes(userTeam);
            resultList.add(teamRes);
        }
        return resultList;
    }


    //팀의 가게 조회
    public List<StoresRes> getMyTeamStores(long teamId, long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<TeamStore> teamStores = team.getTeamStores();
        List<StoresRes> resultList = new ArrayList<>();
        for (TeamStore teamStore : teamStores) {
            StoresRes storesRes = new StoresRes(teamStore);
            resultList.add(storesRes);
        }
        return resultList;
    }

    // 랜덤 비밀번호 생성
    public String generateRandomPassword() {
        String password = UUID.randomUUID().toString();
        return password;
    }
}
