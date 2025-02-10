package com.d111.PrePay.repository;

import com.d111.PrePay.model.QTeam;
import com.d111.PrePay.model.QUser;
import com.d111.PrePay.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepoCustom {


    Optional<Team> findById(Long teamId);

    @Query("SELECT t FROM Team t join FETCH t.teamInitializer WHERE t.publicTeam = true")
    List<Team> findTeamsByPublicTeam(boolean publicTeam);

    Optional<Team> findByTeamPassword(String teamPassword);

    List<Team> findTeamsByTeamNameContaining(String teamName);
}
