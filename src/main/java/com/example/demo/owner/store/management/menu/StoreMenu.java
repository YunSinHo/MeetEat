package com.example.demo.owner.store.management.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_menu")
@Getter
@Setter
public class StoreMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "store_menu_id")
    private Long storeMenuId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private String cost;

    @Column(name = "composition")
    private String composition;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_main", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isMain = false;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;

    public StoreMenu(){}
    public StoreMenu(String imagePath){
        this.imagePath = imagePath;
    }
    
}
