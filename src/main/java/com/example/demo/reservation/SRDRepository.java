package com.example.demo.reservation;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SRDRepository extends JpaRepository<StoreReservationDate, Long>{
    void deleteByDate(LocalDate date);
    boolean existsByDate(LocalDate date);
    void deleteAllByDate(LocalDate yesterday);
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM StoreReservationDate e WHERE e.storeId = :storeId AND e.date = :date")
    boolean existsByDateAndStoreId(@Param("date") LocalDate date, @Param("storeId") Long storeId);
    
}