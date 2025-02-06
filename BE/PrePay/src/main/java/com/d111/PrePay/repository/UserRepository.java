package com.d111.PrePay.repository;

import com.d111.PrePay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {


    User findByEmailAndUserPassword(String userLoginId, String userPassword);
}
