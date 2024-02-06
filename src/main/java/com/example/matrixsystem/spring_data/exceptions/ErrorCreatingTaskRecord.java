package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingTaskRecord extends Exception{
    public ErrorCreatingTaskRecord(){
        super("Не удалось добавить задание в базу данных");
    }
}