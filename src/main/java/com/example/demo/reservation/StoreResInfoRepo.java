package com.example.demo.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreResInfoRepo extends JpaRepository<StoreReservationInfo, Long>{

    Optional<StoreReservationInfo> findByUserIdAndDateAndTime(Long userId, LocalDate date, String time);

    List<StoreReservationInfo> findAllByUserId(Long userId);
    
}
