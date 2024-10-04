package com.example.demo.email;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, String>{

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<EmailCode> findByEmail(String inputEmail);

}
