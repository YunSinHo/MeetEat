package com.example.demo.email;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 이메일 코드 전송
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam(value = "email", required = false) String email) {
        if (email == null)
            return ResponseEntity.badRequest().body("올바른 이메일이 아닙니다.");
        String authCode = VerificationCodeGenerator.generateVerificationCode(); // 가입 인증 코드
        LocalDateTime currentTime = LocalDateTime.now(); // 전송한 시간
        LocalDateTime expirationTime = currentTime.plus(5, ChronoUnit.MINUTES); // 만료 시간
        // 재전송시 이전코드 삭제
        boolean isExistsEmailCode = emailService.isExistsEmailCode(email);
        if (isExistsEmailCode)
            emailService.deleteByEmail(email);

        emailService.saveEmailCode(email, authCode, currentTime, expirationTime);
        emailService.sendSimpleEmail(email, "가입 인증 메일입니다.", authCode);
        System.out.println(authCode);
        return ResponseEntity.ok().build();
    }

    // 코드 확인
    @PostMapping("/check-code")
    public ResponseEntity<Void> checkCode(@RequestParam("inputEmail") String inputEmail,
                                                          @RequestParam("inputCode") String inputCode) {

        EmailCode code = emailService.findByEmail(inputEmail);
        if (code == null) return ResponseEntity.badRequest().build();
        String authCode = code.getVerificationCode();
        if (authCode.equals(inputCode))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
        

    }
}
