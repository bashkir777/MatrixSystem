package com.example.matrixsystem.spring_data.exceptions;

public class ErrorDeletingUserTaskRecord extends Exception{
    public ErrorDeletingUserTaskRecord(){
        super("Не удалось удалить связь user-task в базу данных");
    }
}