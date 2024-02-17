package com.example.matrixsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskForOptionSubmittingDTO {
    private String answer;
    private Integer score;
    private Integer id;
    private Integer module;
}
