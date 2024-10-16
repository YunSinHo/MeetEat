package com.example.demo.owner.store;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long>{

    Optional<Categories> findByName(String food);
    
}
