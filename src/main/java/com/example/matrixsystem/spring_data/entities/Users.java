package com.example.matrixsystem.spring_data.entities;

import com.example.matrixsystem.spring_data.entities.enums.Roles;
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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;

    private String name;

    private String surname;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NONE'")
    private String email;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NONE'")
    private String tg;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NONE'")
    private String phoneNumber;


    private String miroBoard;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "user")
    private Set<UserSection> userSections = new HashSet<>();

    @OneToMany(mappedBy = "userReference", fetch = FetchType.EAGER)
    private Set<UserTask> userTask = new HashSet<>();

    @OneToMany(mappedBy = "userReference", fetch = FetchType.EAGER)
    private Set<UserCustomTask> userCustomTasks = new HashSet<>();
}
