package com.example.demo.owner.store;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "stores")
@Getter
@Setter
@ToString
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "biz_reg_no", nullable = false)
    private String bizRegNo;

    @Column(name = "business", nullable = false)
    private String business;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;


    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = false;

    public Store() {
    }
    public Store(Long ownerId, StoreDTO storeDTO){
        this.ownerId = ownerId;
        this.storeName = storeDTO.getStoreName();
        this.bizRegNo = storeDTO.getBizRegNo();
        this.business = storeDTO.getBusiness();
        this.category = storeDTO.getCategory();
        this.address = storeDTO.getAddress();
        this.lat = storeDTO.getLat();
        this.lng = storeDTO.getLng();
    }
}
