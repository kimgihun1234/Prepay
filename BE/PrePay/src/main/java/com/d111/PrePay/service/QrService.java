package com.d111.PrePay.service;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.model.Qr;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.QrRepository;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import com.d111.PrePay.value.QrType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrService {

    private final UserTeamRepository userTeamRepository;
    private final QrRepository qrRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public StandardRes getPartyQr(String userEmail, Long teamId) {
        Optional<UserTeam> byTeamIdAndUserEmail = userTeamRepository.findByTeamIdAndUser_Email(teamId, userEmail);
        UserTeam userTeam;
        if (byTeamIdAndUserEmail.isPresent()) {
            userTeam = byTeamIdAndUserEmail.get();
        } else {
            return new StandardRes("소속되지 않은 팀입니다. email or teamId 재확인", 400);
        }
        if (userTeam.isPrivilege()) {
            Qr qr = new Qr(QrType.COMPANY_PARTY,userTeam.getUser());
            qrRepository.save(qr);
            return new StandardRes(qr.getUuid(), 201);
        } else {
            throw new RuntimeException("회식 권한이 없습니다.");
        }
    }

    @Transactional
    public StandardRes getPrivateQr(String userEmail, long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        if(team.isPublicTeam()){
            Optional<UserTeam> opUserTeam = userTeamRepository.findByTeamIdAndUser_Email(teamId, userEmail);
            if (opUserTeam.isEmpty()) {
                User user = userRepository.findUserByEmail(userEmail);
                UserTeam userTeam = UserTeam.builder()
                        .team(team)
                        .user(user)
                        .privilege(false)
                        .usageCount(0)
                        .usedAmount(0)
                        .position(false)
                        .build();
                userTeamRepository.save(userTeam);
            }
        }
        User user = userRepository.findUserByEmail(userEmail);
        Qr qr = new Qr(QrType.PRIVATE, user);
        qrRepository.save(qr);
        return new StandardRes(qr.getUuid(),201);
    }
}
