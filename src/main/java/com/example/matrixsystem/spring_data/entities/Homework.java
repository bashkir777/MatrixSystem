package com.example.matrixsystem.spring_data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="creator")
    private Integer creator;

    @OneToMany(mappedBy = "homework", fetch = FetchType.EAGER)
    private Set<HomeworkCustomTask> homeworkCustomTasks = new HashSet<>();
}
