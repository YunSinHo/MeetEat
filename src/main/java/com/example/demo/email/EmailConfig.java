package com.example.demo.email;

import java.util.Properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
// import org.springframework.beans.factory.annotation.Value;

@Configuration
public class EmailConfig {

    // @Value("${MAIL_USERNAME:default_username}")
    // private String username;

    // @Value("${MAIL_PASSWORD:default_password}")
    // private String password;

    // 이메일과 비밀번호를 직접 설정
    private String username = "mallrege@naver.com";   // 본인의 네이버 이메일
    private String password = "tt314936";          // 본인의 네이버 비밀번호

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername(username);	// 메일
        javaMailSender.setPassword(password);	// 패스워드
        javaMailSender.setPort(465);	// 네이버의 경우 465
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");  // TLS 활성화
        properties.setProperty("mail.smtp.ssl.enable", "true");  // SSL 활성화
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.starttls.trust","smtp.naver.com");	//네이버의 경우 stmp.naver.com 변경

        return properties;
    }
}