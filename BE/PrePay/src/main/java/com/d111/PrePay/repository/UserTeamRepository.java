package com.d111.PrePay.repository;

import com.d111.PrePay.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {
}
