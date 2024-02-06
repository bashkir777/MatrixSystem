package com.example.matrixsystem.spring_data.aspects;

import com.example.matrixsystem.security.annotations.RolesAllowed;
import com.example.matrixsystem.spring_data.annotations.HandleDataActionExceptions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@Aspect
public class DataActionsExceptionsHandler {
    @Around("@annotation(handleExceptions)")
    public Object checkRoles(ProceedingJoinPoint joinPoint, HandleDataActionExceptions handleExceptions) throws Throwable{
        try{
            return joinPoint.proceed();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
