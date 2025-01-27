package com.d111.PrePay.service;

import com.d111.PrePay.dto.TeamRequestDTO;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
   

    // 팀 생성
    public Team createTeam(TeamRequestDTO request){
        Team team = Team.builder()
                .teamName(request.getTeamName())
                .publicTeam(request.isPublicTeam())
                .teamPassword(generateRandomPassword(8))
                .dailyPriceLimit(request.getDailyPriceLimit())
                .countLimit(request.getCountLimit())
                .teamMessage(request.getTeamMessage())
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

    //랜던 비밀번호 생성
    public String generateRandomPassword(int length){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

    }
}
