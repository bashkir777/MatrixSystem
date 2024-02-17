package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackForOptionSubmissionDTO {
    // баллы от 0 до 100
    private Integer score;
    // какие задания (по номерам как в ЕГЭ) были выполнены верно
    private List<Integer> modulesDone;
}
