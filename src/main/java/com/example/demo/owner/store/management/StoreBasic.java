package com.example.demo.owner.store.management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_basic")
@Getter
@Setter
public class StoreBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_basic_id")
    private Long storeBasicId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "weekday_start_time")
    private String weekdayStartTime;

    @Column(name = "weekday_end_time")
    private String weekdayEndTime;
    
    @Column(name = "weekend_start_time")
    private String weekendStartTime;

    @Column(name = "weekend_end_time")
    private String weekendEndTime;
    
    @Column(name = "del_min_cost")
    private String delMinCost;

    @Column(name = "del_cost")
    private String delCost;

    @Column(name = "introduction")
    private String introduction;

    public StoreBasic(){}

}
