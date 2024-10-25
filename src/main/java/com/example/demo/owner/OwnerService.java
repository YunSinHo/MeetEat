package com.example.demo.owner;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.user.UserDTO;
import com.example.demo.user.Users;

import java.util.List;
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder){
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // 회원 ID로 유저 찾기
    public Owner findById(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                                   .orElseThrow(() -> new RuntimeException("User not found with ID: " + ownerId));

        return owner;
    }
    // 오너 아이디 값 가져오기
    public List<Long> findAllOwnerId() {
        List<Long> ownerIds = ownerRepository.findAllIds();
        return ownerIds;
    }

    // 회원 아이디로 유저 찾기
    public Owner findByUsername(String username){
        Owner owner = ownerRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return owner;
    }

    // 회원가입 처리
    public void saveOwner(OwnerDTO ownerDTO) {
        String hashValue = passwordEncoder.encode(ownerDTO.getPassword());
        ownerDTO.setPassword(hashValue);

        Owner owner = new Owner();
        owner.setUser(ownerDTO);

        ownerRepository.save(owner);
    }

    // 권한 저장
    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }


    // 아이디 중복 체크
    public boolean isExistsUsername(String username) {

        return ownerRepository.existsByUsername(username);
    }

    public boolean isExistsPhoneNumber(String phoneNumber) {

        return ownerRepository.existsByPhoneNumber(phoneNumber);
    }
    public Long getLoggedInOwnerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Owner owner = ownerRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return owner.getOwnerId();
    }

}
