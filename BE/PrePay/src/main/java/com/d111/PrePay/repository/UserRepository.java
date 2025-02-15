package com.d111.PrePay.repository;

import com.d111.PrePay.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);
    List<User> findAll();

    @Query("SELECT u FROM User u join FETCH u.userTeams ut join FETCH ut.team " +
            "WHERE ut.user.id=:userId")
    User findUserById(@Param("userId") Long userId);
    User findByEmailAndUserPasswordEquals(String userLoginId, String password);
    User findByKakaoId(Long id);
}
