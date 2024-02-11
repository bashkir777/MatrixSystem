package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskForAddingDTO {

    private String task;

    private String answer;

    private String img;

    private Integer module;
}
