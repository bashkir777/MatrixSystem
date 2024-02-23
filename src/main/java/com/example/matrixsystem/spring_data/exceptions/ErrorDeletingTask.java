package com.example.matrixsystem.spring_data.exceptions;

public class ErrorDeletingTask extends Exception{
    public ErrorDeletingTask(){
        super("Не удалось удалить задание");
    }
}
