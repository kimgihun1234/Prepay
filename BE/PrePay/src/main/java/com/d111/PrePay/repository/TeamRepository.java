package com.d111.PrePay.repository;

import com.d111.PrePay.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Long> {
    Optional<Team> findById(Long teamId);

    List<Team> findTeamsByPublicTeam(boolean publicTeam);

    Optional<Team> findByTeamPassword(String teamPassword);

    List<Team> findTeamsByTeamNameContaining(String teamName);
}
