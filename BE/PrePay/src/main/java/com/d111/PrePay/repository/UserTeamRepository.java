package com.d111.PrePay.repository;

import com.d111.PrePay.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {

    Optional<UserTeam> findByTeamIdAndUserId(Long TeamId, Long UserId);
}
