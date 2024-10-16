package com.example.demo.owner.store;

import java.time.LocalDateTime;

import com.example.demo.user.profile.UserProfileDTO;

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

    public Store() {
    }
    public Store(Long ownerId, StoreDTO ownerProfileDTO){
        this.ownerId = ownerId;
        this.storeName = ownerProfileDTO.getName();
        this.bizRegNo = ownerProfileDTO.getBizRegNo();
        this.business = ownerProfileDTO.getBusiness();
        this.category = ownerProfileDTO.getCategory();
        this.address = ownerProfileDTO.getAddress();
    }
}
