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
@Table(name = "store_rank")
@Getter
@Setter
public class StoreRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_rank_id")
    private Long storeRankId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "store_rank")
    private String rank;

    public StoreRank(){}
}
