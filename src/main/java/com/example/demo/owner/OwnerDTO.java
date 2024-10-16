package com.example.demo.owner;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OwnerDTO {

    private Long ownerId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Boolean isActive = true;
    private LocalDateTime createdAt;

    public OwnerDTO(){

    }

    public OwnerDTO(String username, String password, String email, String phoneNumber){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}