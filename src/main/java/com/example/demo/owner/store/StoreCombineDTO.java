package com.example.demo.owner.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreCombineDTO{
    private Long storeId;
    private String storeName;
    private String category;
    private String storeImagePath;
    private String address;
    private String rank;
    private String heart;
    private String startTime;
    private String endTime;
    private Boolean isOpen;

    public StoreCombineDTO(){}
    public StoreCombineDTO(Long storeId, String storeName, String category,
    String storeImagePath, String address, String rank, String heart, String startTime, String endTime, Boolean isOpen) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.category = category;
        this.storeImagePath = storeImagePath;
        this.address = address;
        this.rank = rank;
        this.heart = heart;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpen = isOpen;
    }
}