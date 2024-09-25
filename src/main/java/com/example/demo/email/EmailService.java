package com.example.demo.email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
// 이메일 전송 서비스 클래스
public class EmailService {

    // 수정한내용ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
    // 테스트수정
    @Autowired
    private JavaMailSender mailSender;

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.naver.com");
        mailSender.setPort(465);

        mailSender.setUsername("mallrege@naver.com"); // 여기에 로그인할 이메일 주소를 입력합니다.
        mailSender.setPassword("CW76W9YHFJR1"); // 3rd Party App Password를 사용하세요.

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mallrege@naver.com"); // 발신자 이메일 주소를 설정합니다.
        message.setTo("mallrege@naver.com");
        message.setSubject("Subject");
        message.setText("Email body");

        getJavaMailSender().send(message);
    }
}