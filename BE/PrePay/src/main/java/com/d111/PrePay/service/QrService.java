package com.d111.PrePay.service;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.model.Qr;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.QrRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import com.d111.PrePay.value.QrType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrService {

    private final UserTeamRepository userTeamRepository;
    private final QrRepository qrRepository;
    private final UserRepository userRepository;
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
            return new StandardRes("권한이 없습니다.", 403);
        }
    }

    public StandardRes getPrivateQr(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);
        Qr qr = new Qr(QrType.PRIVATE, user);
        qrRepository.save(qr);
        return new StandardRes(qr.getUuid(),201);
    }
}
