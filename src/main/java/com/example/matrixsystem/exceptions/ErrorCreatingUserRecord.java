package com.example.matrixsystem.exceptions;

public class ErrorCreatingUserRecord extends Exception{
    public ErrorCreatingUserRecord(){
        super("Не удалось добавить задание в базу данных");
    }
}