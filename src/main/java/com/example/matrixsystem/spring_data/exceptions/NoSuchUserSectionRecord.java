package com.example.matrixsystem.spring_data.exceptions;

public class NoSuchUserSectionRecord extends Exception{
    public NoSuchUserSectionRecord(){
        super("В БД нет такого отношения user-section с таким");
    }
}
