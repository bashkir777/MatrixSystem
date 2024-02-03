package com.example.matrixsystem.spring_data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "task")
    private String task;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    private Module module;

    @Column(name = "img")
    private String img;

    @ManyToMany(mappedBy = "tasks")
    private Set<Users> users = new HashSet<>();
}
