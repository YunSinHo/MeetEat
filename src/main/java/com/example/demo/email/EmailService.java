package com.example.demo.email;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private final EmailCodeRepository emailCodeRepository;

    public EmailService(EmailCodeRepository emailCodeRepository){
        this.emailCodeRepository = emailCodeRepository;

    }

    // 이메일 전송 
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("mallrege@naver.com");

        mailSender.send(message);
    }

    // 이메일과 인증코드 저장
    public void saveEmailCode(String email, String authCode, LocalDateTime currentTime, LocalDateTime expirationTime) {
        EmailCode emailCode = new EmailCode();
        emailCode.setEmail(email);
        emailCode.setVerificationCode(authCode);
        emailCode.setCreatedAt(currentTime);
        emailCode.setExpiresAt(expirationTime);
        emailCodeRepository.save(emailCode);
    }

    public boolean isExistsEmailCode(String email) {
        return emailCodeRepository.existsByEmail(email);
    }

    // 재전송시 이메일 코드 삭제
    @Transactional
    public void deleteByEmail(String email) {
        emailCodeRepository.deleteByEmail(email);
    }

    public EmailCode findByEmail(String inputEmail) {
        EmailCode emailCode = emailCodeRepository.findByEmail(inputEmail).orElse(null);

        return emailCode;
    }
}
