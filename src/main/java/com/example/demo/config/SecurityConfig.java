package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                // CSRF 보호를 비활성화합니다.
                httpSecurity.csrf((csrf) -> csrf.disable());

                // 모든 요청을 허용합니다.
                httpSecurity.authorizeHttpRequests((authorize) -> authorize
                                .anyRequest().permitAll());

                // httpSecurity.authorizeHttpRequests((authorize) ->
                // authorize.requestMatchers("/**").permitAll());

                httpSecurity.formLogin((formLogin) -> formLogin.loginPage("/user/login")
                                .defaultSuccessUrl("/index?login")
                                .failureUrl("/user/login?error"));

                httpSecurity.logout((logout) -> logout.logoutUrl("/user/logout")
                                .logoutSuccessUrl("/index?logout=true"));

                

                // 오너 로그인 설정
                // httpSecurity.formLogin((formLogin) -> formLogin
                //                 .loginPage("/owner/login") // 오너 로그인 페이지
                //                 .defaultSuccessUrl("/owner/home?login") // 오너 로그인 성공 후 리다이렉트
                //                 .failureUrl("/owner/login?error")); // 오너 로그인 실패 시 리다이렉트

                // httpSecurity.logout((logout) -> logout
                //                 .logoutUrl("/owner/logout") // 오너 로그아웃 URL
                //                 .logoutSuccessUrl("/index?logout=true")); // 로그아웃 성공 시 리다이렉트

                return httpSecurity.build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
