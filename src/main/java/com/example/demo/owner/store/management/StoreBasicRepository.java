package com.example.demo.owner.store.management;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBasicRepository extends JpaRepository<StoreBasic, Long>{

    Optional<StoreBasic> findByStoreId(Long storeId);
    
}
