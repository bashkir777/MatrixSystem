package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchUserTaskRelation extends Exception{
    public NoSuchUserTaskRelation(){
        super("В базе данных нет пользователя с таким логином");
    }
}