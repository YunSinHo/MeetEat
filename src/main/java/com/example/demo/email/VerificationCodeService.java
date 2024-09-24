// package com.example.project.service;

// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.concurrent.TimeUnit;

// @Service
// public class VerificationCodeService {
//     private final Map<String, String> verificationCodes = new HashMap<>();
//     private final Map<String, Long> expirationTimes = new HashMap<>();
    
//     public String generateVerificationCode(String email) {
//         String code = String.valueOf((int) (Math.random() * 900000) + 100000); // 6자리 랜덤 숫자
//         verificationCodes.put(email, code);
//         expirationTimes.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)); // 5분 유효

//         return code;
//     }

//     public boolean verifyCode(String email, String code) {
//         Long expirationTime = expirationTimes.get(email);
//         if (expirationTime == null || System.currentTimeMillis() > expirationTime) {
//             return false; // 코드가 만료됨
//         }
        
//         return code.equals(verificationCodes.get(email)); // 코드 검증
//     }

//     public void clearCode(String email) {
//         verificationCodes.remove(email);
//         expirationTimes.remove(email);
//     }
// }
