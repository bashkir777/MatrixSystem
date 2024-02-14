package com.example.matrixsystem.spring_data.exceptions;

public class ErrorDeletingUserSection extends Exception{
    public ErrorDeletingUserSection(){
        super("Не удалось удалить отношение user-section");
    }
}
