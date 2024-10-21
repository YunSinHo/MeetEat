package com.example.demo.owner;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.role.Role;
import com.example.demo.role.RoleService;
import com.example.demo.user.UserDTO;
import com.example.demo.user.Users;

import jakarta.transaction.Transactional;


@Controller
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;
    private final RoleService roleService;

    public OwnerController(OwnerService ownerService, RoleService roleService){
        this.ownerService = ownerService;
        this.roleService = roleService;
    }


    // 첫 화면 이후 로그인 or 회원가입 화면
    @GetMapping("/login-join")
    public String loginOrJoin() {
        return "/owner/login-join";
    }

    // 회원가입 방식 선택 페이지로 이동
    @GetMapping("/signup-choice")
    public String signupChoice() {
        return "/owner/signup-choice";
    }

    // 휴대폰 번호로 회원가입
    @GetMapping("/phone-verification")
    public String phoneVerification() {
        return "/owner/phone-verification";
    }

    // 이메일로 회원가입
    @GetMapping("/email-verification")
    public String emailVerification() {
        return "/owner/email-verification";
    }

    // 휴대폰 or 이메일 인증 후 다음 화면
    @PostMapping("/next-join")
    public String nextJoin(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "/owner/next-join";
    }
    // 아이디 중복 확인(일단 이메일로)
    @PostMapping("/isExist-username")
    public ResponseEntity<Map<String, Boolean>> isExistUsername(@RequestParam("username") String username) {
        boolean exist = ownerService.isExistsUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exist);
        return ResponseEntity.ok(response);
    }

    // 전화번호 중복확인
    @PostMapping("/isExist-phoneNumber")
    public ResponseEntity<Map<String, Boolean>> isExistPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {

        boolean exist = ownerService.isExistsPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exist);
        return ResponseEntity.ok(response);
    }

    // 회원가입 처리
    @Transactional
    @PostMapping("/join")
    public String join(@ModelAttribute OwnerDTO ownerDTO) {
        // String rawPassword = userDTO.getPassword();
        ownerDTO.setUsername(ownerDTO.getEmail()); // 이메일을 아이디로 사용
        ownerService.saveOwner(ownerDTO);
        Owner owner = ownerService.findByUsername(ownerDTO.getUsername()); // 저장한 사용자를 다시 조회
        Role userRole = roleService.findByName("ROLE_OWNER"); // 가게 유저 롤
        owner.getRoles().add(userRole); // 롤 추가

        // 변경 사항 저장
        ownerService.saveOwner(owner);

        return "redirect:/login";
    }

    // 전체 설정 메뉴 이동
    @GetMapping("/menu")
    public String menu() {
        return "owner/menu";
    }
    


    
}
