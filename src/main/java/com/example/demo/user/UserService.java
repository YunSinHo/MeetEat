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

    // 회원가입 처리
    public void saveUser(UserDTO userDTO) {
        String hashValue = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hashValue);

        Users newUser = new Users();
        newUser.setUser(userDTO);

        userRepository.save(newUser);
    }

    // 아이디 중복 체크
    public boolean isExistsUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    // 현재 로그인한 사용자의 id
    public Long loggedInUserId(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return user.getUserId();
        
    }

    
}
