package com.d111.PrePay.repository;

import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface TeamStoreRepository extends JpaRepository<TeamStore,Long> {

    @Query("SELECT ts FROM TeamStore ts join fetch ts.store s WHERE ts.team = :team")
    List<TeamStore> findTeamStoresByTeam(@Param("team") Team team);

    @Query("SELECT t FROM Team t join FETCH t.teamStores ts join FETCH ts.store WHERE t.id=:teamId")
    Team findTeamStoresByTeamId(Long teamId);

    TeamStore findTeamStoreByTeamAndStore(Team team, Store store);


    Optional<TeamStore> findByTeamIdAndStoreId(Long teamId, Long storeId);
}
