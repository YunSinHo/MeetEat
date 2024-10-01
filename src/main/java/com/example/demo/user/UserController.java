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
import org.springframework.web.bind.annotation.RequestBody;


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

    // 첫 화면 이후 로그인 or 회원가입 화면
    @GetMapping("/login-join")
    public String loginOrJoin() {
        return "/user/login-join";
    }
    // 회원가입 방식 선택 페이지로 이동
    @GetMapping("/signup-choice")
    public String signupChoice() {
        return "/user/signup-choice";
    }
    
    // 휴대폰 번호로 회원가입
    @GetMapping("/phone-verification")
    public String phoneVerification() {
        return "/user/phone-verification";
    }

    // 이메일로 회원가입
    @GetMapping("/email-verification")
    public String emailVerification() {
        return "/user/email-verification";
    }
    
    // 휴대폰 or 이메일 인증 후 다음 화면
    @PostMapping("/next-join")
    public String nextJoin(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "/user/next-join";
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
    

    // 로그인
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }



    // 두 번째 회원가입페이지
    // @PostMapping("/joinform")
    // public String joinForm(@RequestParam("email") String email, Model model) {
    //     model.addAttribute("email", email);
    //     return "/user/join";
    // }

    

    // 아이디 중복 확인(일단 이메일로)
    @PostMapping("/isExist-username")
    public ResponseEntity<Map<String, Boolean>> isExistUsername(@RequestParam("username") String username) {
        boolean exist = userService.isExistsUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exist);
        return ResponseEntity.ok(response);
    }

    // 전화번호 중복확인
    @PostMapping("/isExist-phoneNumber")
    public ResponseEntity<Map<String, Boolean>> isExistPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        
        boolean exist = userService.isExistsPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exist);
        return ResponseEntity.ok(response);
    }
    
}
