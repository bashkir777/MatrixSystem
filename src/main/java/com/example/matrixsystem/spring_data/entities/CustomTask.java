package com.example.matrixsystem.spring_data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Builder
@Table(name="custom_task")
@NoArgsConstructor
@AllArgsConstructor
public class CustomTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "task", length = Integer.MAX_VALUE)
    private String task;

    @Column(name = "answer", length = Integer.MAX_VALUE)
    private String answer;

    @Column(columnDefinition = "boolean DEFAULT true")
    private Boolean verifiable;

    @Column(name = "img", length = Integer.MAX_VALUE)
    private String img;

    @Column(name = "solution", length = Integer.MAX_VALUE)
    private String solution;

    @OneToMany(mappedBy = "taskReference", fetch = FetchType.EAGER)
    private Set<UserCustomTask> userCustomTasks = new HashSet<>();

    @OneToMany(mappedBy = "customTask", fetch = FetchType.EAGER)
    private List<HomeworkCustomTask> homeworkCustomTasks = new ArrayList<>();
}