package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 ID로 유저 찾기
    public Users findById(Long userId) {
        Users user = userRepository.findById(userId)
                                   .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return user;
    }

    // 회원 아이디로 유저 찾기
    public Users findByUsername(String username){
        Users user = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return user;
    }

    // 회원가입 처리
    public void saveUser(UserDTO userDTO) {
        String hashValue = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashValue);

        Users newUser = new Users();
        newUser.setUser(userDTO);

        userRepository.save(newUser);
    }

    // 권한 저장
    public void saveUser(Users user) {
        userRepository.save(user);
    }


    // 아이디 중복 체크
    public boolean isExistsUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    public boolean isExistsPhoneNumber(String phoneNumber) {

        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    // 현재 로그인한 사용자의 id
    public Long getLoggedInUserId(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Users user = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return user.getUserId();
        
    }


   

    
}
