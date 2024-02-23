package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingCustomTask extends Exception{
    public ErrorCreatingCustomTask(){
        super("не удалось создать кастомное задание");
    }
}
