package com.example.demo.user.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class UserProfileDTO {
    private Long profileId;

    private Long userId;

    private String name;

    private String nickname;

    private Date dateOfBirth;
    
    private Character gender;
}
