package com.example.demo.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    

    // 유저 로그인폼
    @GetMapping("/user-login")
    public String showLoginPage() {
        return "/user/login";
    }

    // 로그인 성공시 메인페이지로 이동
    @GetMapping("/user/main")
    public String userMain() {
        return "/user/main";
    }

}
