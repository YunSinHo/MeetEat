package com.example.demo.user.profile.foodinterest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.Users;

public interface UserFoodCategoryRepository extends JpaRepository<UserFoodCategory, Long> {

    List<UserFoodCategory> findAllByUser(Users user);
        
} 