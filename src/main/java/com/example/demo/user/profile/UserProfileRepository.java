package com.example.demo.user.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.profile.interest.Interest;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

    Optional<UserProfile> findByUserId(Long userId);

    
    boolean existsByUserId(Long userId);
    
}
