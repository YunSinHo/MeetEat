package com.example.demo.user.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;



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

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "nickname", length = 255)
    private String nickname;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "gender", nullable = false)
    private Character gender;

    public UserProfile() {
    }
    public UserProfile(Long userId, UserProfileDTO userProfileDTO, LocalDateTime dateTime ){
        this.userId = userId;
        this.name = userProfileDTO.getName();
        this.nickname = userProfileDTO.getNickname();
        this.gender = userProfileDTO.getGender();
        this.dateOfBirth = dateTime;
    }

}
