package com.example.matrixsystem.exceptions;

public class NoSuchUserInDB extends Exception{
    public NoSuchUserInDB(){
        super("В базе данных нет пользователя с таким логином");
    }
}
