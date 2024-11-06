package com.example.demo.owner.store.management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreTableRepository extends JpaRepository<StoreTable, Long>{

}
