package com.d111.PrePay.repository;

import com.d111.PrePay.model.Likes;
import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Likes findByUserAndStoreAndTeam(User user, Store store, Team team);
}
