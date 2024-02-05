package com.example.matrixsystem.security.aspects;

import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.security.annotations.RolesNotAllowed;
import com.example.matrixsystem.security.beans.UserInformation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class RolesInspector {
    private final UserInformation userInfo;
    @Autowired
    public RolesInspector(UserInformation userInfo){
        this.userInfo = userInfo;
    }
    @Around("@annotation(rolesAllowed)")
    public Object checkRoles(ProceedingJoinPoint joinPoint, RolesAllowed rolesAllowed) throws Throwable{
        String[] allowedRoles = rolesAllowed.value();
        String userRole = userInfo.getUserRole().toString();
        boolean allowed = Arrays.asList(allowedRoles).contains(userRole);
        if  (!allowed){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на данное действие");
        }
        return joinPoint.proceed();
    }
    @Around("@annotation(rolesNotAllowed)")
    public Object checkRoles(ProceedingJoinPoint joinPoint, RolesNotAllowed rolesNotAllowed) throws Throwable{
        String[] notAllowedRoles = rolesNotAllowed.value();
        String userRole = userInfo.getUserRole().toString();
        boolean notAllowed = Arrays.asList(notAllowedRoles).contains(userRole);
        if  (notAllowed){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав на данное действие");
        }
        return joinPoint.proceed();
    }
}
