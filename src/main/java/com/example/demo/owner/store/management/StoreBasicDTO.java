package com.example.demo.owner.store.management;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreBasicDTO {
    private Long storeBasicId;

    private String ownerName;

    private String phoneNumber;
    
    private String delMinCost;

    private String delCost;

    private String introduction;
    
}
