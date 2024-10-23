package com.example.demo.owner.store.management.menu;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreMenuDTO {

    private Long storeId;

    private String name;

    private String cost;

    private String composition;

    private String introduction;

    private String imageName;

    private String imagePath;

    private Boolean isMain = false;
}
