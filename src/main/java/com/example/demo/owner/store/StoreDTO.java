package com.example.demo.owner.store;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {
    private String storeName;

    private String bizRegNo;

    private String business;

    private String category;

    private String address;

    private Double lat;

    private Double lng;
}
