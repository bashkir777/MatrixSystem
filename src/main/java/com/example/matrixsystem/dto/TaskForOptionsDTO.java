package com.example.matrixsystem.dto;

import com.example.matrixsystem.spring_data.entities.Module;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskForOptionsDTO {
    private int id;

    private String task;

    private String answer;

    private String img;

    private ModuleDTO module;
}
