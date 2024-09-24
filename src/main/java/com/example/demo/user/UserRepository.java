package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    
    // 아이디로 해당 유저 찾기
    Optional<Users> findByUsername(String username);

    // 해당 아이디가 이미 존재하는지 확인
    boolean existsByUsername(String username);
}
