package com.example.matrixsystem.exceptions;

public class NoSuchTaskInDB extends Exception{
    public NoSuchTaskInDB(){
        super("В базе данных нет задания с таким ID");
    }
}
