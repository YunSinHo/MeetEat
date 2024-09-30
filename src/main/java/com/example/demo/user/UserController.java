package com.example.demo.user;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    // 회원가입 첫 화면
    @GetMapping("/check-email")
    public String checkEmail() {
        return "/user/check-email";
    }
    
    @GetMapping("/test")
    public String test(){
        return "/user/email_signup";
    }

    // 두 번째 회원가입페이지
    @PostMapping("/joinform")
    public String joinForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "/user/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO userDTO) {
        
        userDTO.setUsername(userDTO.getEmail()); // 이메일을 아이디로 사용
        userService.saveUser(userDTO);
        Users user = new Users();
        user.setUser(userDTO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(userService.loggedInUserId());
        return "redirect:/";
    }

    // 아이디 중복 확인
    @PostMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exist = userService.isExistsUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exist);
        return ResponseEntity.ok(response);
    }
}
