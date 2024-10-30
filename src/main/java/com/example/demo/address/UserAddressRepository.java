package com.example.demo.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long>{

    List<UserAddress> findAllByUserId(Long userId);

    
}