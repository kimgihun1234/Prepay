package com.d111.PrePay.repository;

import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserTeamRepository extends JpaRepository<UserTeam,Long>,UserTeamCustom {

//    @Query("SELECT ut FROM UserTeam ut join fetch ")
    Optional<UserTeam> findByTeamIdAndUserId(Long teamId, Long userId);

    boolean existsByUserAndTeam(User user, Team team);

    Optional<UserTeam> findByTeam_IdAndUser_Email(Long teamId, String userEmail);

    Optional<UserTeam> findByTeamIdAndUser_Email(Long teamId, String changeUserEmail);

    void deleteByTeam_Id(Long teamId);
}
