package com.example.demo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.HashMap;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail() {
        String authCode = VerificationCodeGenerator.generateVerificationCode();
        emailService.sendSimpleEmail("mallrege@naver.com", "가입 인증 메일입니다.", authCode);
        System.out.println(authCode);
        return ResponseEntity.ok(authCode);
    }

    @PostMapping("/check-code")
    public ResponseEntity<Map<String, Boolean>> checkCode(@RequestParam("authCode") String authCode,
            @RequestParam("inputCode") String inputCode) {
        boolean isValidCode;
        if(authCode.equals(inputCode)) isValidCode = true;
        else isValidCode = false;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValidCode", isValidCode);
        System.out.println("인증번호" + authCode + " " + "입력한 인증번호" + inputCode + isValidCode);
        return ResponseEntity.ok(response);
    }

}