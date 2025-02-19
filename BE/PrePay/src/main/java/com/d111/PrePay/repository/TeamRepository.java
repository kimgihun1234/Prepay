package com.d111.PrePay.repository;

import com.d111.PrePay.model.QTeam;
import com.d111.PrePay.model.QUser;
import com.d111.PrePay.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepoCustom {

    @Query("SELECT t FROM Team t join FETCH t.userTeams ut join FETCH ut.user WHERE t.id=:teamId")
    Team findTeamWithUserTeamAndUserByTeamId(@Param("teamId") Long teamId);

    Optional<Team> findById(Long teamId);

    @Query("SELECT t FROM Team t join FETCH t.teamInitializer WHERE t.publicTeam = true")
    List<Team> findTeamsWithUserByPublicTeam(boolean publicTeam);


    Optional<Team> findByTeamPassword(String teamPassword);
}
