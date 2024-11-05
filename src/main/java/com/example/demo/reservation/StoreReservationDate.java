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
@Getter
@Setter
@Table(name = "store_reservation_date")
public class StoreReservationDate {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long dateId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "storeId")
    private Long storeId;

    @Column(name = "time")
    private String time;

    public StoreReservationDate(){

    }

    public StoreReservationDate(LocalDate date, Long storeId, String time) {
        this.date = date;
        this.storeId = storeId;
        this.time = time;
    }
    
}
