package com.example.demo.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    
    // 일반 유저 로그인
    @GetMapping("/user/form")
    public String userLoginForm() {
        return "/user/login";
    }
}
