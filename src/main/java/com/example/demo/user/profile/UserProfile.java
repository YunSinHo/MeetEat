package com.example.demo.user.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@ToString
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "nickname", length = 255)
    private String nickname;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    public UserProfile(){}
    
    
}
