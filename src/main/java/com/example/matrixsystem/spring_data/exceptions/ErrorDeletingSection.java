package com.example.matrixsystem.spring_data.exceptions;

public class ErrorDeletingSection extends Exception{
    public ErrorDeletingSection(){
        super("Не удалось удалить секцию");
    }
}
