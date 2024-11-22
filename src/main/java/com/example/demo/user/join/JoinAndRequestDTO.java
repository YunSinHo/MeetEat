package com.example.demo.user.join;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinAndRequestDTO {
    
    private List<JoinRequest> request;
    private JoinInfo info;
    public JoinAndRequestDTO(List<JoinRequest> request, JoinInfo info){
        this.request = request;
        this.info = info;
    }
}
