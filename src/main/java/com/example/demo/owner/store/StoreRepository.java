package com.example.demo.owner.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{

    boolean existsByOwnerId(Long ownerId);

    
}
