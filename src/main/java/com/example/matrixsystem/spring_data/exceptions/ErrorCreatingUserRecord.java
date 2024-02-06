package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingUserRecord extends Exception{
    public ErrorCreatingUserRecord(){
        super("Не удалось добавить задание в базу данных");
    }
}