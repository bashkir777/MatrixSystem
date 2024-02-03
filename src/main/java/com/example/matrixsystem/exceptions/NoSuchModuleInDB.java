package com.example.matrixsystem.exceptions;

public class NoSuchModuleInDB extends Exception{
    public NoSuchModuleInDB(){
        super("В базе данных нет модуля с таким ID");
    }
}
