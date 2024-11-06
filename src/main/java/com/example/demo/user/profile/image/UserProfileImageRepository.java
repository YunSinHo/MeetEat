package com.example.demo.user.profile.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.user.Users;

@Repository
public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long> {

    List<UserProfileImage> findAllByUser(Users user);

    void deleteByImageName(String imageName);

    // 대표 이미지 경로 가져오기
    @Query("SELECT e.imagePath FROM UserProfileImage e WHERE e.user.userId = :userId AND e.isMain = :isMain")
    String findByUserIdAndIsMain(@Param("userId") Long userId, @Param("isMain") Boolean isMain);

}
