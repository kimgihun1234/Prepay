package com.d111.PrePay.repository;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface TeamStoreRepository extends JpaRepository<TeamStore,Long> {
    List<TeamStore> findTeamStoresByTeam(Team team);
    Optional<TeamStore> findByTeamIdAndStoreId(Long teamId, Long storeId);
}
