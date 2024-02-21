package com.example.matrixsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleDTO {
    private Integer id;
    private Boolean verifiable;
    private Integer maxPoints;
    private String name;
}
