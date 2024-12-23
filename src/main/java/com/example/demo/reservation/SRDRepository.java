package com.example.demo.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SRDRepository extends JpaRepository<StoreReservationDate, Long> {
    void deleteByDate(LocalDate date);

    boolean existsByDate(LocalDate date);

    void deleteAllByDate(LocalDate yesterday);

    // // 어플리케이션 실행시 데이터 체크
    // @Query("SELECT COUNT(e) > 0 FROM StoreReservationDate e WHERE e.date = :date AND e.storeId = :storeId")
    boolean existsByDateAndStoreId(@Param("date") LocalDate date, @Param("storeId") Long storeId);
    

    @Query("SELECT e FROM StoreReservationDate e WHERE e.storeId = :storeId AND e.date = :date AND e.time = :time")
    Optional<StoreReservationDate> findByIdAndDateAndTime(@Param("storeId")long storeId,
     @Param("date")LocalDate date, @Param("time")String time);

    
    
    List<StoreReservationDate> findAllByStoreId(Long storeId);

    StoreReservationDate findByStoreIdAndDateAndTime(long long1, LocalDate localDate, String time);

}