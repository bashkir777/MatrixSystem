package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingOption extends Exception{
    public ErrorCreatingOption(){
        super("Не удалось создать вариант");
    }
}
