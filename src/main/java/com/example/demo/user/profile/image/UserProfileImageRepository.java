package com.example.demo.user.profile.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.Users;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long>{

    List<UserProfileImage> findAllByUser(Users user);

    void deleteByImageName(String imageName);
    
}
