package com.example.matrixsystem.spring_data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name="task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "task", length = Integer.MAX_VALUE)
    private String task;

    @Column(name = "answer", length = Integer.MAX_VALUE)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    private Module module;
    @Column(name = "img", length = Integer.MAX_VALUE)
    private String img;

    @Column(name = "solution", length = Integer.MAX_VALUE)
    private String solution;

    @OneToMany(mappedBy = "taskReference", fetch = FetchType.EAGER)
    private Set<UserTask> userTask = new HashSet<>();

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
    private Set<OptionTask> optionTasks = new HashSet<>();
}
