package com.d111.PrePay.repository;

import com.d111.PrePay.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {

    Optional<UserTeam> findByTeamIdAndUserId(Long TeamId, Long UserId);
}
