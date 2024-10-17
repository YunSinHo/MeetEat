package com.example.demo.user.profile.interest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.Users;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long>{


    List<UserInterest> findAllByUser(Users user);
    
}
