package com.example.demo.owner.store.management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "store_table")

public class StoreTable {
    @Id
    @Column(name = "storeId", nullable = false) 
    private Long storeId;
    @Column(name = "one_table")
    private Integer oneTable;
    @Column(name = "two_table")
    private Integer twoTable;
    @Column(name = "four_table")
    private Integer fourTable;
    @Column(name = "party_table")
    private Integer partyTable;

    public StoreTable(){
        
    }
    
}
