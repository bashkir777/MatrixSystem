package com.example.matrixsystem.security.beans;

import com.example.matrixsystem.beans.DatabaseManager;
import com.example.matrixsystem.spring_data.entities.Users;
import com.example.matrixsystem.spring_data.entities.enums.Roles;
import com.example.matrixsystem.spring_data.exceptions.NoSuchUserInDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
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

    public Users getUser() throws NoSuchUserInDB {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return manager.getUserByLogin(username);
    }

}
