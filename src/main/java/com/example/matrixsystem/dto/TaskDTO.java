package com.example.matrixsystem.dto;

import com.example.matrixsystem.spring_data.entities.Module;
import com.example.matrixsystem.spring_data.entities.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TaskDTO {

    private String task;

    private String answer;

    private String img;

    private Integer module;
}
