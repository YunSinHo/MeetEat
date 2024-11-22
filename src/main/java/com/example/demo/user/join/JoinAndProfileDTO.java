package com.example.demo.user.join;

import com.example.demo.user.profile.UserProfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinAndProfileDTO {
    
    private UserProfile profile;
    private JoinInfo info;

    public JoinAndProfileDTO(UserProfile profile, JoinInfo info){
        this.profile = profile;
        this.info = info;
    }
}
