package com.example.demo.owner.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_heart")
@Getter
@Setter
public class StoreHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_heart_id")
    private Long storeHeartId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "heart")
    private String heart;

    public StoreHeart(){}
}
