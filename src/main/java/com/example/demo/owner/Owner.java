package com.example.demo.owner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.Collection;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.role.Role;

import java.util.HashSet;

@Entity
@Table(name = "owners", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
@Getter
@Setter
@ToString
public class Owner implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;

    @Column(name = "create_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "owner_roles", joinColumns = @JoinColumn(name = "owner_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Owner() {

    }

    public void setUser(OwnerDTO ownerDTO) {
        this.username = ownerDTO.getEmail();
        this.password = ownerDTO.getPassword();
        this.email = ownerDTO.getEmail();
        this.phoneNumber = ownerDTO.getPhoneNumber();
    }

    // UserDetails 인터페이스 메서드 구현

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Role의 이름을 GrantedAuthority로 변환
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성 여부
    }
}
