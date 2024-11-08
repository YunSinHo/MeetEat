package com.example.demo.reservation;

import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationBasicDTO {
    
    private String userId;
    private String storeId;
    private String storeName;
    private String date;
    private String time;
    private String table;
    private Integer totalCost;

}
