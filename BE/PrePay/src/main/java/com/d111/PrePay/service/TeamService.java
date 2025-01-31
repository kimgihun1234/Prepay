package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

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


    // 랜덤 비밀번호 생성
    public String generateRandomPassword() {
        String password = UUID.randomUUID().toString();

        return password;
    }
}
