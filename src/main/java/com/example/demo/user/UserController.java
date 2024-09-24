package com.example.demo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 로그인
    @GetMapping("/user/login")
    public String login() {
        int ddddddddddddddddd;
        return "/user/login";
    }

    // 회원가입페이지
    @GetMapping("/user/joinform")
    public String joinForm() {
        return "/user/join";
    }

    // 회원가입 처리
    @PostMapping("/user/join")
    public String join(@ModelAttribute UserDTO userDTO) {
        userService.saveUser(userDTO);
        return "/user/login";
    }

    // 아이디 중복 확인
    @PostMapping("/user/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = userService.isExist(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    

}
