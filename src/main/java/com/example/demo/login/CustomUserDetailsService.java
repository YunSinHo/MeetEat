package com.example.demo.login;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.owner.Owner;
import com.example.demo.owner.OwnerRepository;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
           // 일반 사용자 검색
        Users user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities // 사용자 권한 포함
            );
        }

        // 오너 검색
        Owner owner = ownerRepository.findByUsername(username).orElse(null);
        if (owner != null) {
            List<GrantedAuthority> authorities = owner.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    owner.getUsername(),
                    owner.getPassword(),
                    authorities // 오너 권한 포함
            );
        }
        throw new UsernameNotFoundException("User not found");
    }
}
