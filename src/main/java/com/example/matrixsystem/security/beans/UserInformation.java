package com.example.matrixsystem.security.beans;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Component
public class UserInformation {

    private final DatabaseManager manager;

    @Autowired
    public UserInformation(DatabaseManager manager){
        this.manager = manager;
    }

    public Roles getUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return manager.getUserRoleByLogin(username);
    }

}
