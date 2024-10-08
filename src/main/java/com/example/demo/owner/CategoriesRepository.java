package com.example.demo.owner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriesRepository extends JpaRepository<Categories, Long>{

    Optional<Categories> findByName(String food);
    
}
