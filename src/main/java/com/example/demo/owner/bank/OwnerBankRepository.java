package com.example.demo.owner.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OwnerBankRepository extends JpaRepository<OwnerBank, Long>{

    
} 
