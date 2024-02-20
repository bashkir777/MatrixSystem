package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskAnswerDTO {
    private String answer;
    private String solution;
}
