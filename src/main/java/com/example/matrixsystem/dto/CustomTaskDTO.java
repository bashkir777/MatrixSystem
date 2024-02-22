package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomTaskDTO {
    public Integer id;
    public String task;
    private String answer;
    private String solution;
    private String img;
    private String status;
}
