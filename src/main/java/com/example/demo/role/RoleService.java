package com.example.demo.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;

    }

    // 롤 찾기
    public Role findByName(String name){
        Role role = roleRepository.findByName(name)
                                  .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));

        return role;
    }
}
