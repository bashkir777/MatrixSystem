package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskForAddingDTO {

    private String task;

    private String answer;

    private byte[] img;

    private String solution;

    private Integer module;
}
