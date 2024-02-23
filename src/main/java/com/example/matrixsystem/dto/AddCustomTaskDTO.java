package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AddCustomTaskDTO {
    private String task;

    private String answer;

    private MultipartFile image;

    private String solution;
}
