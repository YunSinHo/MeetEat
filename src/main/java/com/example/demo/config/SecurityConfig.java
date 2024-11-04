package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                httpSecurity.csrf((csrf) -> csrf.disable());

                httpSecurity
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

                httpSecurity
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/login/user/main").hasRole("USER")
                                                .requestMatchers("/login/owner/main").hasRole("OWNER")
                                                .anyRequest().permitAll())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login") // 공통 로그인 페이지
                                                .loginProcessingUrl("/process-login") // 공통 로그인 처리 URL
                                                .successHandler((request, response, authentication) -> {
                                                        // 로그인 성공 후 권한에 따른 리다이렉트 처리
                                                        if (authentication.getAuthorities().stream()
                                                                        .anyMatch(grantedAuthority -> grantedAuthority
                                                                                        .getAuthority()
                                                                                        .equals("ROLE_USER"))) {
                                                                response.sendRedirect("/login/user/main"); // ROLE_USER

                                                        } else if (authentication.getAuthorities().stream()
                                                                        .anyMatch(grantedAuthority -> grantedAuthority
                                                                                        .getAuthority()
                                                                                        .equals("ROLE_OWNER"))) {
                                                                response.sendRedirect("/login/owner/main"); // ROLE_OWNER

                                                        } else {
                                                                response.sendRedirect("/"); // 그 외 경우
                                                        }
                                                })
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/index?logout=true"))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> response
                                                                .sendRedirect("/login")))
                                .httpBasic(AbstractHttpConfigurer::disable); // httpBasic 비활성화

                return httpSecurity.build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

}
