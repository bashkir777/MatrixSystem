package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchCustomTaskException extends Exception{
    public NoSuchCustomTaskException(){
        super("Нет кастомного задания с таким id");
    }
}
