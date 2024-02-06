package com.example.matrixsystem.spring_data.exceptions;


public class ErrorCreatingUserTaskRecord extends Exception{
    public ErrorCreatingUserTaskRecord(){
        super("Не удалось добавить связь user-task в базу данных");
    }
}