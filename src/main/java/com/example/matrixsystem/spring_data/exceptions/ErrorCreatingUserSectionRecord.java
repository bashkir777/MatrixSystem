package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingUserSectionRecord extends Exception{
    public ErrorCreatingUserSectionRecord(){
        super("Не удалось создать отношение user-section");
    }
}
