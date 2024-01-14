package com.example.matrixsystem.spring_data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="module_task")
public class ModuleTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "module_id")
    private Integer moduleId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
