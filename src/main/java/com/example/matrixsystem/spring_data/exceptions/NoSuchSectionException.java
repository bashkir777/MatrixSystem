package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchSectionException extends Exception{
    public NoSuchSectionException(){
        super("Секция не найдена");
    }
}
