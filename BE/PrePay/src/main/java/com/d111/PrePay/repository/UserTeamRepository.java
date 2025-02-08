package com.d111.PrePay.repository;

import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {

    Optional<UserTeam> findByTeamIdAndUserId(Long teamId, Long userId);

    boolean existsByUserAndTeam(User user, Team team);

    Optional<UserTeam> findByTeam_IdAndUser_Email(Long teamId, String userEmail);
}
