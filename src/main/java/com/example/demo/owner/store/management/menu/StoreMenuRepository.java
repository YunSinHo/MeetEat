package com.example.demo.owner.store.management.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long>{

    List<StoreMenu> findAllByStoreId(Long storeId);
    
}
