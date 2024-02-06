package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchModuleInDB extends Exception{
    public NoSuchModuleInDB(){
        super("В базе данных нет модуля с таким ID");
    }
}
