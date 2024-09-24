package com.example.demo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    // @PostMapping("/signup")
    // public ResponseEntity<String> signUp(@RequestParam String email) {
    //     String verificationCode = VerificationCodeGenerator.generateVerificationCode();
    //     try {
    //         emailService.sendVerificationEmail(email, verificationCode);
    //         return ResponseEntity.ok("Verification email sent to " + email);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                             .body("Failed to send email: " + e.getMessage());
    //     }
    // }
    @GetMapping("/send-email")
    public String sendEmail() {
        emailService.sendSimpleMessage();
        return "redirect:/user/login";
    }
    
    
}
