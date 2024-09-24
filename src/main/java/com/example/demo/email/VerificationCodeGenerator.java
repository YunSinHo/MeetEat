package com.example.demo.email;

import java.util.Random;

// 난수 생성 유틸리티 클래스
public class VerificationCodeGenerator {

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));  // 6자리 난수 생성
        }
        return code.toString();
    }
}