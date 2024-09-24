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
    private String name;
    private String email;
    private String phoneNumber;
    private Character gender;
    private Boolean isActive = true;
    private LocalDateTime createdAt;

    public UserDTO(){

    }

    public UserDTO(String username, String password, String name, String email, String phoneNumber, Character gender){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

}
