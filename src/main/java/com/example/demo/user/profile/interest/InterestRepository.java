package com.example.demo.user.profile.interest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.profile.UserProfile;

public interface InterestRepository extends JpaRepository<Interest, Long>{

    Optional<Interest> findByName(String interest);
    
}
