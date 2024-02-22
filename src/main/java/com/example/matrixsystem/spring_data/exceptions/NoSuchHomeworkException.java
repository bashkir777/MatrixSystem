package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchHomeworkException extends Exception{
    public NoSuchHomeworkException(){
        super("Не удалось найти домашнюю работу с таким id");
    }
}
