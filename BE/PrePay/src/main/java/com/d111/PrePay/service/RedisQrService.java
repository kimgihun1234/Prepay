package com.d111.PrePay.service;

import com.d111.PrePay.dto.respond.StandardRes;
import com.d111.PrePay.model.RedisQr;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import com.d111.PrePay.value.QrType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisQrService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    @Transactional
    public StandardRes getRedisQr(String email, long teamId) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Team team = teamRepository.findById(teamId).orElseThrow();
        if(team.isPublicTeam()){
            Optional<UserTeam> opUserTeam = userTeamRepository.findByTeamIdAndUser_Email(teamId, email);
            if (opUserTeam.isEmpty()) {
                User user = userRepository.findUserByEmail(email);
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
        RedisQr redisQr = new RedisQr(QrType.PRIVATE, email);
        valueOperations.set(redisQr.getUuid(), redisQr);
        redisTemplate.expire(redisQr.getUuid(), 30, TimeUnit.SECONDS);
        return new StandardRes(redisQr.getUuid(), 200);
    }
}
