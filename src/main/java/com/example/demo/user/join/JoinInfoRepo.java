package com.example.demo.user.join;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JoinInfoRepo extends JpaRepository<JoinInfo, Long>{

    @Query("SELECT e FROM JoinInfo e WHERE e.userId =:userId AND endDate >:currentTime")
    List<JoinInfo> findAllByUserIdAndEndDate(@Param("userId")Long userId, @Param("currentTime")LocalDateTime currentTime);
    @Query("SELECT e FROM JoinInfo e WHERE e.otherId =:userId AND endDate >:currentTime")
    List<JoinInfo> findAllByOtherIdAndEndDate(@Param("userId")Long userId, @Param("currentTime")LocalDateTime currentTime);
    @Query("SELECT e FROM JoinInfo e WHERE e.isAccept =:isAccept AND endDate >:currentTime")
    List<JoinInfo> findAllByEndDateAndIsAccept(@Param("isAccept")boolean isAccept, @Param("currentTime")LocalDateTime currDateTime);
    
}
