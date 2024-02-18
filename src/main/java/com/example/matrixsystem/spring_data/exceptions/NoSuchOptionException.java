package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchOptionException extends Exception{
    public NoSuchOptionException(){
        super("В базе данных нет варианта с таким id");
    }
}
