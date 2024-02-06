package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchTaskInDB extends Exception{
    public NoSuchTaskInDB(){
        super("В базе данных нет задания с таким ID");
    }
}
