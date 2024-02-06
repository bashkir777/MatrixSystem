package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchUserInDB extends Exception{
    public NoSuchUserInDB(){
        super("В базе данных нет пользователя с таким логином");
    }
}
