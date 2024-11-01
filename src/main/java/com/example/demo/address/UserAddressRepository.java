package com.example.demo.address;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long>{

    List<UserAddress> findAllByUserId(Long userId);

    @Query("SELECT e FROM UserAddress e WHERE e.userId = :userId AND e.isActive = :isActive")
    Optional<UserAddress> findByUserIdAndIsActive(@Param("userId")Long userId, @Param("isActive") Boolean isActive);

    
}