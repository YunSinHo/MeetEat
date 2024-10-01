package com.example.demo.user;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Boolean isActive = true;
    private LocalDateTime createdAt;

    public UserDTO(){

    }

    public UserDTO(String username, String password, String email, String phoneNumber){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
