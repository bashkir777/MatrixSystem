package com.example.matrixsystem.spring_data.exceptions;

public class ErrorCreatingHomeworkCustomTask extends Exception{
    public ErrorCreatingHomeworkCustomTask(){
        super("Не удалось добавить отношение Homework - Custom tasks");
    }
}
