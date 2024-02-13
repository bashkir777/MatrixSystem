package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingSection  extends Exception{
    public ErrorCreatingSection(){
        super("Не удалось создать секцию");
    }
}
