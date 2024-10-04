package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.login.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                // CSRF 보호를 비활성화합니다.
                httpSecurity.csrf((csrf) -> csrf.disable());

                // 모든 요청을 허용합니다.
                // 요청에 대한 권한 설정
                httpSecurity.authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/login/user/main", "/profile/set-user")
                                .hasRole("USER") // 이 경로들은 USER 권한 필요
                                .anyRequest().permitAll()); // 그 외 요청은 모두 허용

                httpSecurity.formLogin(formLogin -> formLogin
                                .loginPage("/login/user-login")
                                .defaultSuccessUrl("/login/user/main")
                                .failureHandler((request, response, exception) -> {
                                        System.out.println("Login failed: " + exception.getMessage());
                                        response.sendRedirect("/login/user-login?error=true");
                                }));

                httpSecurity.logout((logout) -> logout.logoutUrl("/user/logout")
                                .logoutSuccessUrl("/index?logout=true"));

                httpSecurity.exceptionHandling(handling -> handling
                                .authenticationEntryPoint((request, response, authException) -> {
                                        System.out.println("Authentication failed: " + authException.getMessage());
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                }));

                return httpSecurity.build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

}
