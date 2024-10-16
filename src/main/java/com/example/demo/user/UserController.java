package com.example.demo.user;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

import com.example.demo.role.Role;
import com.example.demo.role.RoleService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

    // 회원가입 처리
    @Transactional
    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO userDTO) {
        // String rawPassword = userDTO.getPassword();
        userDTO.setUsername(userDTO.getEmail()); // 이메일을 아이디로 사용
        userService.saveUser(userDTO);
        Users user = userService.findByUsername(userDTO.getUsername()); // 저장한 사용자를 다시 조회
        Role userRole = roleService.findByName("ROLE_USER"); // 일반 유저 롤
        user.getRoles().add(userRole); // 롤 추가

        // 변경 사항 저장
        userService.saveUser(user);

        return "redirect:/login";
    }



}
