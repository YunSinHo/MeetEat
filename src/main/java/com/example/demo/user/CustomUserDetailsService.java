package com.example.demo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> result = userRepository.findByUsername(username);
 
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Users user = result.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getUsername().equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN-USER"));
        }
        else {
            authorities.add(new SimpleGrantedAuthority("NORMAL-USER"));
        }

        return new User(user.getUsername(), user.getPassword(), authorities);

    }
    
}
