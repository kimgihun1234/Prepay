package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.TeamCreateStoreReq;
import com.d111.PrePay.dto.request.TeamDetailReq;
import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.respond.*;
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

    // 팀 가맹점 추가
    public TeamStore createStore(TeamCreateStoreReq req) {
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
                .teamInitializer(userRepository.findById(request.getUserId()).orElseThrow())
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


    //팀 가맹점의 좌표 조회
    public List<StoresCorRes> getStoresCor(long teamId, long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<TeamStore> list = teamStoreRepository.findTeamStoresByTeam(team);
        List<StoresCorRes> result = new ArrayList<>();
        for (TeamStore teamStore : list) {
            Store store = teamStore.getStore();
            StoresCorRes storesCorRes = new StoresCorRes(store);
            result.add(storesCorRes);
        }
        return result;
    }

    //퍼블릭 팀 리스트 조회
    public List<PublicTeamsRes> getPublicTeams() {
        List<Team> teams = teamRepository.findTeamsByPublicTeam(true);
        List<PublicTeamsRes> resultList = new ArrayList<>();
        for (Team team : teams) {
            PublicTeamsRes publicTeamsRes = new PublicTeamsRes(team);
            publicTeamsRes.setTeamInitializerNickname(team.getTeamInitializer().getNickname());
            resultList.add(publicTeamsRes);
        }
        return resultList;
    }

    // 랜덤 비밀번호 생성
    public String generateRandomPassword() {
        String password = UUID.randomUUID().toString();
        return password;
    }
}
