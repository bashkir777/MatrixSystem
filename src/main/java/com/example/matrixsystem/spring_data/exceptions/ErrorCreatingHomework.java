package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingHomework extends Exception{
    public ErrorCreatingHomework(){
        super("Не удалось создать домашнее задание");
    }
}
