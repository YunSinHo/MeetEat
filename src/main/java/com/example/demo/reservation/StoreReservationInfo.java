package com.example.demo.reservation;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_reservation_info")
@Getter
@Setter
public class StoreReservationInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "storeId")
    private Long storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "returnable_date")
    private LocalDate returnableDate;

    @Column(name = "time")
    private String time;

    @Column(name = "is_complete")
    private Boolean isComplete = false;

    public StoreReservationInfo(){}
}
