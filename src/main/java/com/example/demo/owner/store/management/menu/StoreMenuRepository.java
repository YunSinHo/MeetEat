package com.example.demo.owner.store.management.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.owner.store.management.StoreBasic;

public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long>{

    List<StoreMenu> findAllByStoreId(Long storeId);

    Optional<StoreMenu> findByStoreMenuId(Long menuId);
    
}
